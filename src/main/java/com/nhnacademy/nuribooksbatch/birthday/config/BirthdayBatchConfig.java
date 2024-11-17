package com.nhnacademy.nuribooksbatch.birthday.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.nhnacademy.nuribooksbatch.birthday.dto.MemberInfoDto;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.Value;

// @Configuration
// @RequiredArgsConstructor
// public class BirthdayBatchConfig {
//
// 	private final EntityManagerFactory entityManagerFactory;
// 	private final JobRepository jobRepository;
// 	private final Step sendBirthdayCoupon;
// 	private final DataSource dataSource;
// 	private final PlatformTransactionManager transactionManager;
//
// 	@Bean
// 	public Job birthdayCouponJob() {
// 		return new JobBuilder("birthdayCouponJob", jobRepository)
// 			.start(sendBirthdayCoupon())
// 			.build();
// 	}
//
// 	@Bean
// 	public Step sendBirthdayCoupon() {
// 		return new StepBuilder("sendCoupon", jobRepository)
// 			.<MemberInfoDto, MemberInfoDto>chunk(1000, transactionManager)
// 			.reader(birthdayMemberItemReader())
// 			.writer(birthdayMemberItemWriter())
// 			.build();
// 	}
//
// 	@Bean
// 	public JpaPagingItemReader<MemberInfoDto> birthdayMemberItemReader() {
// 		return new JpaPagingItemReaderBuilder<MemberInfoDto>()
// 			.entityManagerFactory(entityManagerFactory)
// 			.queryString("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.birthDate) = :month ORDER BY m.id ASC")
// 			.pageSize(10)
// 			.name("birthdayMemberItemReader")
// 			.build();
// 	}
//
// 	@Bean
// 	public JpaItemWriter<MemberInfoDto> birthdayMemberItemWriter() {
// 		return new JpaItemWriterBuilder<MemberInfoDto>()
// 			.entityManagerFactory(entityManagerFactory)
// 			.usePersist(true)
// 			.build();
// 	}
//
//
//
// }
@Configuration
@RequiredArgsConstructor
public class BirthdayBatchConfig {
	private final EntityManagerFactory entityManagerFactory;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job birthdayCouponJob() throws Exception {
		return new JobBuilder("birthdayCouponJob", jobRepository)
			.start(sendBirthdayCoupon())
			.build();
	}

	@Bean
	public Step sendBirthdayCoupon() throws Exception {
		return new StepBuilder("sendCoupon", jobRepository)
			.<MemberInfoDto, MemberInfoDto>chunk(1000, transactionManager)
			.reader(birthdayMemberItemReader())
			.writer(birthdayMemberItemWriter())
			.build();
	}

	@Bean
	public JpaPagingItemReader<MemberInfoDto> birthdayMemberItemReader(

	) throws Exception {
		return new JpaPagingItemReaderBuilder<MemberInfoDto>()
			.entityManagerFactory(entityManagerFactory)
			.queryString("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.birthday) = :month ORDER BY m.id ASC")
			.pageSize(10)
			.queryProvider((JpaQueryProvider)queryProvider())
			.name("birthdayMemberItemReader")
			.build();
	}

	private PagingQueryProvider queryProvider() throws Exception {
		LocalDate nowDate = LocalDate.now();
		int month = nowDate.getMonthValue();

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("SELECT customer_id, birthday");
		queryProvider.setFromClause("FROM member");
		queryProvider.setWhereClause("WHERE MONTH(birthday) = " + month);

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("customer_id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		return queryProvider;
	}

	@Bean
	public JpaItemWriter<MemberInfoDto> birthdayMemberItemWriter() {
		return new JpaItemWriterBuilder<MemberInfoDto>()
			.entityManagerFactory(entityManagerFactory)
			.usePersist(true)
			.build();
	}
}
