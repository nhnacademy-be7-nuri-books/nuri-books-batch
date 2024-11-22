package com.nhnacademy.nuribooksbatch.member;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;

	// localhost:8085/test1?value=1 과 같이 호출하면 해당 Job이 실행됨.
	// value 파라미터는 단지 jobParameter의 설정을 위해서 추가함.
	// 동일한 Job에 대해 jobParameter가 동일하면 이미 실행된 Job이라 실행이 안되므로 다른 parameter를 설정해 주어야 함.
	@GetMapping("/test1")
	public String loginBatchTest(@RequestParam("value") String value) throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("value", value)
			.toJobParameters();

		// 원하는 Job의 이름을 넣어주면 됨.
		jobLauncher.run(jobRegistry.getJob("inactiveMembersByLastLoginJob"), jobParameters);

		return "OK";
	}
}
