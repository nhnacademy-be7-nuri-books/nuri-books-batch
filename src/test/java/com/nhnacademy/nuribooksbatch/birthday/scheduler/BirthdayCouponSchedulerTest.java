package com.nhnacademy.nuribooksbatch.birthday.scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nhnacademy.nuribooksbatch.birthday.config.BirthdayBatchConfig;

@SpringBootTest(classes = {BirthdayBatchConfig.class, BirthdayCouponSchedulerTest.TestBatchConfig.class})
@SpringBatchTest
@RunWith(SpringRunner.class)
class BirthdayCouponSchedulerTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void runBirthCouponJob() throws Exception {
		// Arrange
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("CouponJobTime", System.currentTimeMillis())
			.toJobParameters();

		// Act
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		// Assert
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@TestConfiguration
	static class TestBatchConfig {
		@Bean
		public JobLauncherTestUtils jobLauncherTestUtils() {
			return new JobLauncherTestUtils();
		}
	}
}

