package com.nhnacademy.nuribooksbatch.member.withdraw.job;

import java.sql.SQLException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.common.listener.JobLoggingListener;
import com.nhnacademy.nuribooksbatch.common.listener.StepLoggingListener;
import com.nhnacademy.nuribooksbatch.member.withdraw.dto.WithdrawnCustomerIdDto;
import com.nhnacademy.nuribooksbatch.member.withdraw.listener.SoftDeleteWriteListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SoftDeleteMembersJob {

	private static final Integer CHUNK_SIZE = 10;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final JdbcPagingItemReader<WithdrawnCustomerIdDto> membersWithdrawnStatusReader;
	private final CompositeItemWriter<WithdrawnCustomerIdDto> softDeleteMembersWriter;
	private final JobLoggingListener jobLoggingListener;
	private final StepLoggingListener stepLoggingListener;
	private final SoftDeleteWriteListener softDeleteWriteListener;

	@Bean
	public Job softDeleteMembersByWithdrawnStatusJob() {

		return new JobBuilder("softDeleteMembersByWithdrawnStatusJob", jobRepository)
			.start(checkMembersWithdrawnStatusStep())
			.listener(jobLoggingListener)
			.build();
	}

	@Bean
	public Step checkMembersWithdrawnStatusStep() {

		return new StepBuilder("checkMembersWithdrawnStatusStep", jobRepository)
			.<WithdrawnCustomerIdDto, WithdrawnCustomerIdDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(membersWithdrawnStatusReader)
			.writer(softDeleteMembersWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(softDeleteWriteListener)
			.build();
	}
}
