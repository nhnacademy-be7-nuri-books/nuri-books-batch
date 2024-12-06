package com.nhnacademy.nuribooksbatch.member;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchController {

	private static final String CONTROLLER_TEST = "controllerTest";
	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;

	@GetMapping("/inactive")
	public ResponseEntity<Void> inactiveMembersByLastLoginJob() throws
		JobExecutionAlreadyRunningException,
		NoSuchJobException,
		JobInstanceAlreadyCompleteException,
		JobRestartException,
		JobParametersInvalidException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString(CONTROLLER_TEST, LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("inactiveMembersByLastLoginJob"), jobParameters);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/withdraw")
	public ResponseEntity<Void> softDeleteMembersByWithdrawnStatusJob() throws
		JobExecutionAlreadyRunningException,
		NoSuchJobException,
		JobInstanceAlreadyCompleteException,
		JobRestartException,
		JobParametersInvalidException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString(CONTROLLER_TEST, LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("softDeleteMembersByWithdrawnStatusJob"), jobParameters);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/grade")
	public ResponseEntity<Void> updateMembersGradeByTotalPaymentAmountNewJob() throws
		JobExecutionAlreadyRunningException,
		NoSuchJobException,
		JobInstanceAlreadyCompleteException,
		JobRestartException,
		JobParametersInvalidException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString(CONTROLLER_TEST, LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("updateMembersGradeByTotalPaymentAmountNewJob"), jobParameters);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/birthday")
	public ResponseEntity<Void> issueBirthdayCouponByBirthdayCouponJob() throws
		JobExecutionAlreadyRunningException,
		NoSuchJobException,
		JobInstanceAlreadyCompleteException,
		JobRestartException,
		JobParametersInvalidException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString(CONTROLLER_TEST, LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("issueBirthdayCouponByBirthdayCouponJob"), jobParameters);

		return ResponseEntity.ok().build();
	}
}
