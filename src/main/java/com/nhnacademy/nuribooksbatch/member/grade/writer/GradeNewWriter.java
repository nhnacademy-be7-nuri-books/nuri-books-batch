package com.nhnacademy.nuribooksbatch.member.grade.writer;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.nuribooksbatch.member.grade.dto.MemberGradeBatchDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GradeNewWriter {

	private final DataSource dataSource;

	@Bean
	JdbcBatchItemWriter<MemberGradeBatchDto> memberGradeWriter() {

		String gradeUpdateSql = "UPDATE members m "
			+ "SET m.grade_id = :gradeId, "
			+ "m.total_payment_amount = 0 "
			+ "WHERE m.customer_id = :customerId";

		return new JdbcBatchItemWriterBuilder<MemberGradeBatchDto>()
			.dataSource(dataSource)
			.sql(gradeUpdateSql)
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.build();
	}
}
