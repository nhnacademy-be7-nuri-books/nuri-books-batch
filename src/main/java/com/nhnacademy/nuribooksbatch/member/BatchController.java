package com.nhnacademy.nuribooksbatch.member;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchController {

	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;

	@GetMapping("/inactive")
	public ResponseEntity<Void> inactiveBatchTest() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("controllerTest", LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("inactiveMembersByLastLoginJob"), jobParameters);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/withdraw")
	public ResponseEntity<Void> softDeleteBatchTest() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("controllerTest", LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("softDeleteMembersByWithdrawnStatusJob"), jobParameters);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/grade")
	public ResponseEntity<Void> gradeUpdateBatchTest() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("controllerTest", LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("updateMembersGradeByTotalPaymentAmountJob"), jobParameters);

		return ResponseEntity.ok().build();
	}
}
