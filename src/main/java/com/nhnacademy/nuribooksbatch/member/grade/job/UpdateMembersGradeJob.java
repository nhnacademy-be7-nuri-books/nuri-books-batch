package com.nhnacademy.nuribooksbatch.member.grade.job;

import java.sql.SQLException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.common.listener.JobLoggingListener;
import com.nhnacademy.nuribooksbatch.common.listener.StepLoggingListener;
import com.nhnacademy.nuribooksbatch.member.grade.dto.GradeUpdateCustomerIdDto;
import com.nhnacademy.nuribooksbatch.member.grade.listener.GradeWriteListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateMembersGradeJob {

	private static final Integer CHUNK_SIZE = 10;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final JobLoggingListener jobLoggingListener;
	private final StepLoggingListener stepLoggingListener;
	private final GradeWriteListener gradeWriteListener;

	@Autowired
	@Qualifier("standardMembersReader")
	JdbcPagingItemReader<GradeUpdateCustomerIdDto> standardMembersReader;

	@Autowired
	@Qualifier("royalMembersReader")
	JdbcPagingItemReader<GradeUpdateCustomerIdDto> royalMembersReader;

	@Autowired
	@Qualifier("goldMembersReader")
	JdbcPagingItemReader<GradeUpdateCustomerIdDto> goldMembersReader;

	@Autowired
	@Qualifier("platinumMembersReader")
	JdbcPagingItemReader<GradeUpdateCustomerIdDto> platinumMembersReader;

	@Autowired
	@Qualifier("standardGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> standardGradeWriter;

	@Autowired
	@Qualifier("royalGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> royalGradeWriter;

	@Autowired
	@Qualifier("goldGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> goldGradeWriter;

	@Autowired
	@Qualifier("platinumGradeWriter")
	JdbcBatchItemWriter<GradeUpdateCustomerIdDto> platinumGradeWriter;

	@Bean
	public Job updateMembersGradeByTotalPaymentAmountJob() {

		return new JobBuilder("updateMembersGradeByTotalPaymentAmountJob", jobRepository)
			.start(standardMembersGradeUpdateStep())
			.next(royalMembersGradeUpdateStep())
			.next(goldMembersGradeUpdateStep())
			.next(platinumMembersGradeUpdateStep())
			.listener(jobLoggingListener)
			.build();
	}

	// 첫번째로 STANDARD 등급의 회원을 업데이트
	@Bean
	public Step standardMembersGradeUpdateStep() {

		return new StepBuilder("standardMembersGradeUpdateStep", jobRepository)
			.<GradeUpdateCustomerIdDto, GradeUpdateCustomerIdDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(standardMembersReader)
			.writer(standardGradeWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(gradeWriteListener)
			.build();
	}

	// 두번째로 ROYAL 등급의 회원을 업데이트
	@Bean
	public Step royalMembersGradeUpdateStep() {

		return new StepBuilder("royalMembersGradeUpdateStep", jobRepository)
			.<GradeUpdateCustomerIdDto, GradeUpdateCustomerIdDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(royalMembersReader)
			.writer(royalGradeWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(gradeWriteListener)
			.build();
	}

	// 세번째로 GOLD 등급의 회원을 업데이트
	@Bean
	public Step goldMembersGradeUpdateStep() {

		return new StepBuilder("goldMembersGradeUpdateStep", jobRepository)
			.<GradeUpdateCustomerIdDto, GradeUpdateCustomerIdDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(goldMembersReader)
			.writer(goldGradeWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(gradeWriteListener)
			.build();
	}

	// 네번째로 PLATINUM 등급의 회원을 업데이트
	@Bean
	public Step platinumMembersGradeUpdateStep() {

		return new StepBuilder("platinumMembersGradeUpdateStep", jobRepository)
			.<GradeUpdateCustomerIdDto, GradeUpdateCustomerIdDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(platinumMembersReader)
			.writer(platinumGradeWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(gradeWriteListener)
			.build();
	}
}
