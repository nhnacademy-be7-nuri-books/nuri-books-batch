package com.nhnacademy.nuribooksbatch.member.withdraw.writer;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.nuribooksbatch.member.withdraw.dto.WithdrawnCustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SoftDeleteWriter {

	private final DataSource dataSource;

	@Bean
	public CompositeItemWriter<WithdrawnCustomerIdDto> softDeleteMembersWriter() {

		// CompositeItemWriter로 두 개의 Writer를 조합
		CompositeItemWriter<WithdrawnCustomerIdDto> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(memberWriter(), customerWriter()));
		return writer;
	}

	@Bean
	JdbcBatchItemWriter<WithdrawnCustomerIdDto> memberWriter() {

		// 첫 번째 SQL: members 테이블 업데이트
		String memberSql = "UPDATE members SET "
			+ "authority = NULL, "
			+ "grade_id = NULL, "
			+ "gender = NULL, "
			+ "birthday = NULL, "
			+ "created_at = NULL, "
			+ "point = 0, "
			+ "total_payment_amount = 0, "
			+ "latest_login_at = NULL, "
			+ "withdrawn_at = NULL "
			+ "WHERE customer_id = :customer_id";

		return new JdbcBatchItemWriterBuilder<WithdrawnCustomerIdDto>()
			.dataSource(dataSource)
			.sql(memberSql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<WithdrawnCustomerIdDto> customerWriter() {

		// 두 번째 SQL: customers 테이블 업데이트
		String customerSql = "UPDATE customers SET "
			+ "name = '', "
			+ "password = '', "
			+ "phone_number = '', "
			+ "email = '' "
			+ "WHERE customer_id = :customer_id";

		return new JdbcBatchItemWriterBuilder<WithdrawnCustomerIdDto>()
			.dataSource(dataSource)
			.sql(customerSql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}
}
