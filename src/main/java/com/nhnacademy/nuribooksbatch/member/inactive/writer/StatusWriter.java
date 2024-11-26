package com.nhnacademy.nuribooksbatch.member.inactive.writer;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.nuribooksbatch.member.inactive.dto.InactiveCustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StatusWriter {

	private final DataSource dataSource;

	// checkMembersLastLogin Step을 실행하는 Writer를 정의한다.
	@Bean
	public JdbcBatchItemWriter<InactiveCustomerIdDto> changeMemberStatusWriter() {

		String sql = "UPDATE members SET status = 'INACTIVE', total_payment_amount = 0 "
			+ "WHERE customer_id = :customer_id";

		return new JdbcBatchItemWriterBuilder<InactiveCustomerIdDto>()
			.dataSource(dataSource)
			.sql(sql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}
}
