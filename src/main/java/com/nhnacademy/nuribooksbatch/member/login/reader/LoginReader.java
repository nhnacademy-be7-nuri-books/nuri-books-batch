package com.nhnacademy.nuribooksbatch.member.login.reader;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.nhnacademy.nuribooksbatch.member.login.dto.CustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoginReader {

	private final DataSource dataSource;

	// checkMembersLastLogin Step을 실행하는 Reader를 정의한다.
	@Bean
	public JdbcPagingItemReader<CustomerIdDto> membersLastLoginReader() {

		return new JdbcPagingItemReaderBuilder<CustomerIdDto>()
			.name("membersLastLoginReader")
			.dataSource(dataSource)
			.selectClause("SELECT customer_id")
			.fromClause("FROM members")
			.whereClause("WHERE status = 'ACTIVE' AND latest_login_at <= DATE_SUB(CURRENT_DATE, INTERVAL 3 MONTH)")
			// .whereClause("WHERE username = 'member27'")
			.sortKeys(Map.of("customer_id", Order.ASCENDING))
			.rowMapper(new BeanPropertyRowMapper<>(CustomerIdDto.class))
			.pageSize(10)
			.build();
	}
}
