package com.nhnacademy.nuribooksbatch.member.withdraw.reader;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.nhnacademy.nuribooksbatch.member.withdraw.dto.WithdrawnCustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WithdrawnStatusReader {

	private static final Integer PAGE_SIZE = 10;
	private final DataSource dataSource;

	@Bean
	public JdbcPagingItemReader<WithdrawnCustomerIdDto> membersWithdrawnStatusReader() {

		return new JdbcPagingItemReaderBuilder<WithdrawnCustomerIdDto>()
			.name("membersWithdrawnStatusReader")
			.dataSource(dataSource)
			.selectClause("SELECT customer_id")
			.fromClause("FROM members")
			.whereClause("WHERE status = 'WITHDRAWN' AND withdrawn_at <= DATE_SUB(CURRENT_DATE, INTERVAL 1 YEAR)")
			.sortKeys(Map.of("customer_id", Order.ASCENDING))
			.rowMapper(new BeanPropertyRowMapper<>(WithdrawnCustomerIdDto.class))
			.pageSize(PAGE_SIZE)
			.build();
	}
}
