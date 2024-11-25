package com.nhnacademy.nuribooksbatch.member.inactive.job;

import java.sql.SQLException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.common.listener.JobLoggingListener;
import com.nhnacademy.nuribooksbatch.common.listener.StepLoggingListener;
import com.nhnacademy.nuribooksbatch.member.inactive.dto.InactiveCustomerIdDto;
import com.nhnacademy.nuribooksbatch.member.inactive.listener.StatusWriteListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InactiveMembersJob {

	private static final Integer CHUNK_SIZE = 10;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final JdbcBatchItemWriter<InactiveCustomerIdDto> changeMembersStatusWriter;
	private final JdbcPagingItemReader<InactiveCustomerIdDto> membersLastLoginReader;
	private final JobLoggingListener jobLoggingListener;
	private final StepLoggingListener stepLoggingListener;
	private final StatusWriteListener statusWriteListener;

	// 최근 로그인 일시를 확인하는 Job을 정의한다.
	@Bean
	public Job inactiveMembersByLastLoginJob() {

		return new JobBuilder("inactiveMembersByLastLoginJob", jobRepository)
			.start(checkMembersLastLoginStep())
			.listener(jobLoggingListener)
			.build();
	}

	// inactiveMembersByLastLogin Job을 실행하는 Step을 정의한다.
	@Bean
	public Step checkMembersLastLoginStep() {

		return new StepBuilder("checkMembersLastLoginStep", jobRepository)
			.<InactiveCustomerIdDto, InactiveCustomerIdDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(membersLastLoginReader)
			.writer(changeMembersStatusWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(statusWriteListener)
			.build();
	}
}
