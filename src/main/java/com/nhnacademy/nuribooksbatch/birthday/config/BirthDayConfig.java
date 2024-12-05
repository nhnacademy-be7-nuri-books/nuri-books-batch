package com.nhnacademy.nuribooksbatch.birthday.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.birthday.domain.Member;
import com.nhnacademy.nuribooksbatch.common.sender.MessageRequest;
import com.nhnacademy.nuribooksbatch.common.sender.MessageSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthDayConfig {

	private final DataSource dataSource;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final JobLauncher jobLauncher;
	private final MessageSender messageSender;

	@Scheduled(cron = "0 30 0 1 * ?")
	public void runJobAtScheduledTime() {
		try {
			jobLauncher.run(birthdayCouponJob(sendCouponStep()), new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		} catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException
				 | JobParametersInvalidException | JobRestartException e) {
			messageSender.sendMessage(new MessageRequest("누리북스 배치 알림봇",
				"스케줄러 에러 - 생일 쿠폰 발행 작업에 문제가 발생하였습니다. "
					+ "관리자는 해당 배치 작업을 확인하십시오. : " + e.getMessage()));
		}
	}

	@Bean
	public Job birthdayCouponJob(Step sendCouponStep) {
		return new JobBuilder("issueBirthdayCouponByBirthdayCouponJob", jobRepository)
			.start(sendCouponStep)
			.build();
	}

	@Bean
	public Step sendCouponStep() {
		return new StepBuilder("sendCouponStep", jobRepository)
			.<Member, Member>chunk(10, platformTransactionManager)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public JdbcPagingItemReader<Member> itemReader() {
		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("customer_id", Order.ASCENDING);

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("SELECT customer_id");
		queryProvider.setFromClause("FROM members");
		queryProvider.setSortKeys(sortKeys);

		String currentMonthCondition = "WHERE MONTH(birthday) = MONTH(CURRENT_DATE())";
		queryProvider.setWhereClause(currentMonthCondition);

		return new JdbcPagingItemReaderBuilder<Member>()
			.name("memberItemReader")
			.dataSource(dataSource)
			.pageSize(100)
			.queryProvider(queryProvider)
			.rowMapper(new MemberRowMapper())
			.build();
	}

	@Bean
	public ItemWriter<Member> itemWriter() {
		return new ItemWriter<>() {
			private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			@Override
			public void write(Chunk<? extends Member> items) {
				Long couponId = null;

				// 쿠폰 조회
				String sql = "SELECT coupon_id FROM coupons WHERE expire_date IS NULL " +
					"AND (name LIKE '%birthday%' OR name LIKE '%생일%') LIMIT 1";
				try {
					couponId = jdbcTemplate.queryForObject(sql, Long.class);
				} catch (DataAccessException e) {
					log.error("쿠폰 조회 중 오류 발생", e);
					sendErrorNotification("쿠폰 조회 중 문제가 발생하였습니다. 관리자는 해당 배치 작업을 확인하십시오.");
					return;
				}

				if (couponId == null) {
					log.warn("생일 쿠폰이 존재하지 않습니다.");
					sendErrorNotification("생일 쿠폰이 존재하지 않습니다. 관리자는 해당 배치 작업을 확인하십시오.");
					return;
				}

				// 쿠폰 발급
				for (Member member : items) {
					String insertSql =
						"INSERT INTO member_coupons (customer_id, coupon_id, created_at, expired_at, is_used) " +
							"VALUES (?, ?, NOW(), LAST_DAY(CURDATE()), false)";
					jdbcTemplate.update(insertSql, member.getCustomerId(), couponId);
				}
			}

			private void sendErrorNotification(String message) {
				messageSender.sendMessage(new MessageRequest("누리북스 배치 알림봇",
					"스케줄러 에러 - 생일 쿠폰 발행 작업에 문제가 발생하였습니다. " + message));
			}
		};
	}
}
