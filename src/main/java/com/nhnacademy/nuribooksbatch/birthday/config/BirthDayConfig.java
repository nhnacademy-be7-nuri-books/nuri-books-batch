package com.nhnacademy.nuribooksbatch.birthday.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.birthday.domain.Member;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BirthDayConfig {

	private final DataSource businessDataSource;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager batchTransactionManager;

	@Bean
	public Job birthdayCouponJob(JobRepository jobRepository, Step sendCouponStep) {
		return new JobBuilder("birthdayCouponJob", jobRepository)
			.start(sendCouponStep)
			.build();
	}

	@Bean
	public Step sendCouponStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws
		Exception {
		return new StepBuilder("sendCouponStep", jobRepository)
			.<Member, Member>chunk(10, transactionManager)
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
		queryProvider.setFromClause("FROM member");
		queryProvider.setSortKeys(sortKeys);

		return new JdbcPagingItemReaderBuilder<Member>()
			.name("memberItemReader")
			.dataSource(businessDataSource)
			.pageSize(100)
			.queryProvider(queryProvider)
			.rowMapper(new BeanPropertyRowMapper<>(Member.class))
			.build();
	}

	@Bean
	public ItemWriter<Member> itemWriter() {
		return items -> items.forEach(System.out::println);
	}
}

