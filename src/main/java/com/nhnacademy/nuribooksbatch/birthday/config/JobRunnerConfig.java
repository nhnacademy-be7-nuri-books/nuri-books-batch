package com.nhnacademy.nuribooksbatch.birthday.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobRunnerConfig {

	private final JobLauncher jobLauncher;
	private final Job birthdayCouponJob;

	public JobRunnerConfig(JobLauncher jobLauncher, Job birthdayCouponJob) {
		this.jobLauncher = jobLauncher;
		this.birthdayCouponJob = birthdayCouponJob;
	}

	@Bean
	public ApplicationRunner runJob() {
		return args -> {
			jobLauncher.run(birthdayCouponJob, new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		};
	}
}
