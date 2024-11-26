package com.nhnacademy.nuribooksbatch.member.inactive.reader;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.nhnacademy.nuribooksbatch.member.inactive.dto.InactiveCustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoginReader {

	private static final Integer PAGE_SIZE = 10;
	private final DataSource dataSource;

	// checkMembersLastLogin Step을 실행하는 Reader를 정의한다.
	@Bean
	public JdbcPagingItemReader<InactiveCustomerIdDto> membersLastLoginReader() {

		return new JdbcPagingItemReaderBuilder<InactiveCustomerIdDto>()
			.name("membersLastLoginReader")
			.dataSource(dataSource)
			.selectClause("SELECT customer_id")
			.fromClause("FROM members")
			.whereClause("WHERE status = 'ACTIVE' AND latest_login_at <= DATE_SUB(CURRENT_DATE, INTERVAL 3 MONTH)")
			.sortKeys(Map.of("customer_id", Order.ASCENDING))
			.rowMapper(new BeanPropertyRowMapper<>(InactiveCustomerIdDto.class))
			.pageSize(PAGE_SIZE)
			.build();
	}
}
