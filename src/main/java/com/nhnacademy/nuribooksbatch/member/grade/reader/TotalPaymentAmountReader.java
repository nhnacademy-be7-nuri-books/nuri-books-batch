package com.nhnacademy.nuribooksbatch.member.grade.reader;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.nhnacademy.nuribooksbatch.member.grade.dto.GradeUpdateCustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TotalPaymentAmountReader {

	private static final Integer PAGE_SIZE = 10;
	private final DataSource dataSource;

	@Bean
	@Qualifier("royalMembersReader")
	public JdbcPagingItemReader<GradeUpdateCustomerIdDto> royalMembersReader() {

		return new JdbcPagingItemReaderBuilder<GradeUpdateCustomerIdDto>()
			.name("royalMembersReader")
			.dataSource(dataSource)
			.selectClause("SELECT customer_id")
			.fromClause("FROM members")
			.whereClause("WHERE 100_000 <= total_payment_amount AND total_payment_amount < 200_000")
			.sortKeys(Map.of("customer_id", Order.ASCENDING))
			.rowMapper(new BeanPropertyRowMapper<>(GradeUpdateCustomerIdDto.class))
			.pageSize(PAGE_SIZE)
			.build();
	}

	@Bean
	@Qualifier("goldMembersReader")
	public JdbcPagingItemReader<GradeUpdateCustomerIdDto> goldMembersReader() {

		return new JdbcPagingItemReaderBuilder<GradeUpdateCustomerIdDto>()
			.name("goldMembersReader")
			.dataSource(dataSource)
			.selectClause("SELECT customer_id")
			.fromClause("FROM members")
			.whereClause("WHERE 200_000 <= total_payment_amount AND total_payment_amount < 300_000")
			.sortKeys(Map.of("customer_id", Order.ASCENDING))
			.rowMapper(new BeanPropertyRowMapper<>(GradeUpdateCustomerIdDto.class))
			.pageSize(PAGE_SIZE)
			.build();
	}

	@Bean
	@Qualifier("platinumMembersReader")
	public JdbcPagingItemReader<GradeUpdateCustomerIdDto> platinumMembersReader() {

		return new JdbcPagingItemReaderBuilder<GradeUpdateCustomerIdDto>()
			.name("platinumMembersReader")
			.dataSource(dataSource)
			.selectClause("SELECT customer_id")
			.fromClause("FROM members")
			.whereClause("WHERE 300_000 <= total_payment_amount")
			.sortKeys(Map.of("customer_id", Order.ASCENDING))
			.rowMapper(new BeanPropertyRowMapper<>(GradeUpdateCustomerIdDto.class))
			.pageSize(PAGE_SIZE)
			.build();
	}
}
