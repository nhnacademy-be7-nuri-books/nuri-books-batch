package com.nhnacademy.nuribooksbatch.member.grade.job;

import java.sql.SQLException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.common.listener.JobLoggingListener;
import com.nhnacademy.nuribooksbatch.common.listener.StepLoggingListener;
import com.nhnacademy.nuribooksbatch.member.grade.dto.MemberGradeBatchDto;
import com.nhnacademy.nuribooksbatch.member.grade.listener.GradeNewWriteListener;
import com.nhnacademy.nuribooksbatch.member.grade.reader.MemberGradeIdNewReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateMembersGradeNewJob {

	private static final Integer CHUNK_SIZE = 10;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final MemberGradeIdNewReader memberGradeIdNewReader;
	private final JdbcBatchItemWriter<MemberGradeBatchDto> gradeNewWriter;
	private final JobLoggingListener jobLoggingListener;
	private final StepLoggingListener stepLoggingListener;
	private final GradeNewWriteListener gradeNewWriteListener;

	@Bean
	public Job updateMembersGradeByTotalPaymentAmountNewJob() {

		return new JobBuilder("updateMembersGradeByTotalPaymentAmountNewJob", jobRepository)
			.start(standardMembersGradeUpdateNewStep())
			.listener(jobLoggingListener)
			.build();
	}

	@Bean
	public Step standardMembersGradeUpdateNewStep() {

		return new StepBuilder("standardMembersGradeUpdateNewStep", jobRepository)
			.<MemberGradeBatchDto, MemberGradeBatchDto>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(memberGradeIdNewReader)
			.writer(gradeNewWriter)
			.faultTolerant()
			.retry(ConnectTimeoutException.class)
			.retry(PessimisticLockingFailureException.class)
			.noRetry(SQLException.class)
			.retryLimit(2)
			.listener(stepLoggingListener)
			.listener(gradeNewWriteListener)
			.build();
	}
}
