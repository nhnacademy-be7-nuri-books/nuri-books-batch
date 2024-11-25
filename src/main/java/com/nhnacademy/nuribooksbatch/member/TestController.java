package com.nhnacademy.nuribooksbatch.member;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;

	@GetMapping("/inactive")
	public String inactiveBatchTest() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("controllerTest", LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("inactiveMembersByLastLoginJob"), jobParameters);

		return "OK";
	}

	@GetMapping("/withdraw")
	public String softDeleteBatchTest() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("controllerTest", LocalDateTime.now().toString())
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("softDeleteMembersByWithdrawnStatusJob"), jobParameters);

		return "OK";
	}
}
