// package com.nhnacademy.nuribooksbatch.birthday;
//
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.test.JobLauncherTestUtils;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.transaction.PlatformTransactionManager;
//
// @TestConfiguration
//  class TestBatchConfig {
// 	@Bean
// 	public JobLauncherTestUtils jobLauncherTestUtils(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
// 		JobLauncherTestUtils testUtils = new JobLauncherTestUtils();
// 		testUtils.setJobRepository(jobRepository);
// 		testUtils.setTransactionManager(transactionManager);
// 		return testUtils;
// 	}
// }
//
