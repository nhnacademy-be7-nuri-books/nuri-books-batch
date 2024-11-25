package com.nhnacademy.nuribooksbatch.member.grade.writer;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.nuribooksbatch.member.grade.dto.GradeUpdateCustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GradeWriter {

	private final DataSource dataSource;

	@Bean
	@Qualifier("royalGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> royalGradeWriter() {

		String royalGradeSql = "UPDATE members m "
			+ "SET m.grade_id = "
			+ "(SELECT g.grade_id "
			+ "FROM grades g "
			+ "WHERE g.name = 'ROYAL') "
			+ "WHERE m.customer_id = :customer_id";

		return new JdbcBatchItemWriterBuilder<GradeUpdateCustomerIdDto>()
			.dataSource(dataSource)
			.sql(royalGradeSql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}

	@Bean
	@Qualifier("goldGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> goldGradeWriter() {

		String goldGradeSql = "UPDATE members m "
			+ "SET m.grade_id = "
			+ "(SELECT g.grade_id "
			+ "FROM grades g "
			+ "WHERE g.name = 'GOLD') "
			+ "WHERE m.customer_id = :customer_id";

		return new JdbcBatchItemWriterBuilder<GradeUpdateCustomerIdDto>()
			.dataSource(dataSource)
			.sql(goldGradeSql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}

	@Bean
	@Qualifier("platinumGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> platinumGradeWriter() {

		String platinumGradeSql = "UPDATE members m "
			+ "SET m.grade_id = "
			+ "(SELECT g.grade_id "
			+ "FROM grades g "
			+ "WHERE g.name = 'PLATINUM') "
			+ "WHERE m.customer_id = :customer_id";

		return new JdbcBatchItemWriterBuilder<GradeUpdateCustomerIdDto>()
			.dataSource(dataSource)
			.sql(platinumGradeSql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}
}
