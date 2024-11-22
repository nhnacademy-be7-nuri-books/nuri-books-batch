package com.nhnacademy.nuribooksbatch.member.login.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.member.login.dto.CustomerIdDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InactiveMembersJob {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final JdbcBatchItemWriter<CustomerIdDto> changeMembersStatusWriter;
	private final JdbcPagingItemReader<CustomerIdDto> membersLastLoginReader;
	private final StepExecutionListener stepLoggingListener;

	// 최근 로그인 일시를 확인하는 Job을 정의한다.
	@Bean
	public Job inactiveMembersByLastLoginJob() {

		return new JobBuilder("inactiveMembersByLastLoginJob", jobRepository)
			.start(checkMembersLastLoginStep())
			.build();
	}

	// inactiveMembersByLastLogin Job을 실행하는 Step을 정의한다.
	@Bean
	public Step checkMembersLastLoginStep() {

		return new StepBuilder("checkMembersLastLoginStep", jobRepository)
			.<CustomerIdDto, CustomerIdDto>chunk(10, platformTransactionManager)
			.reader(membersLastLoginReader)
			.writer(changeMembersStatusWriter)
			.listener(stepLoggingListener)
			.build();
	}
}
