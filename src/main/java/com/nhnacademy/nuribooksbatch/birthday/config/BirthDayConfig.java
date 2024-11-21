package com.nhnacademy.nuribooksbatch.birthday.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.birthday.domain.Member;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BirthDayConfig {

	private final DataSource dataSource;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final JobLauncher jobLauncher;

	@Scheduled(cron = "0 30 0 1 * *")
	public void runJobAtScheduledTime() {
		try {
			jobLauncher.run(birthdayCouponJob(sendCouponStep()), new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean
	public Job birthdayCouponJob(Step sendCouponStep) {
		return new JobBuilder("birthdayCouponJob", jobRepository)
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

		// String currentMonthCondition = "WHERE MONTH(birthday) = MONTH(CURRENT_DATE())";
		String currentMonthCondition = "WHERE MONTH(birthday) = MONTH(CURRENT_DATE())";
		queryProvider.setWhereClause(currentMonthCondition);

		return new JdbcPagingItemReaderBuilder<Member>()
			.name("memberItemReader")
			.dataSource(dataSource)
			.pageSize(100)
			.queryProvider(queryProvider)
			.rowMapper(new BeanPropertyRowMapper<>(Member.class))
			.build();
	}

	@Bean
	public ItemWriter<Member> itemWriter() {
		return new ItemWriter<>() {
			private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			@Override
			public void write(Chunk<? extends Member> items) {
				for (Member member : items) {
					String sql = "SELECT coupon_id FROM coupons WHERE expire_date IS NULL " +
						"AND (name LIKE '%birthday%' OR name LIKE '%생일%') LIMIT 1";
					Long couponId = jdbcTemplate.queryForObject(sql, Long.class);

					if (couponId != null) {
						String insertSql =
							"INSERT INTO member_coupons (customer_id, coupon_id, created_at, expired_at, is_used) " +
								"VALUES (?, ?, NOW(), LAST_DAY(CURDATE()), false)";
						jdbcTemplate.update(insertSql, member.getCustomer_id(), couponId);

						System.out.println(
							"쿠폰 발급: customer_id = " + member.getCustomer_id() + ", coupon_id = " + couponId);
					} else {
						System.out.println("활성화된 생일 쿠폰이 없습니다.");
					}
				}
			}

		};
	}
}
