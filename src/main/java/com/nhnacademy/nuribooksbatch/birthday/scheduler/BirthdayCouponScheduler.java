package com.nhnacademy.nuribooksbatch.birthday.scheduler;

import java.time.LocalDate;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.birthday.config.BirthdayBatchConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BirthdayCouponScheduler {
	private final JobLauncher jobLauncher;
	private final BirthdayBatchConfig birthdayBatchConfig;

	// @Scheduled(cron = "0 1 0 1 * *")
	// public void runBirthCouponJob() {
	// 	try {
	// 		JobParameters jobParameters = new JobParametersBuilder()
	// 			.addLong("CouponJobTime", System.currentTimeMillis())
	// 			.toJobParameters();
	// 		jobLauncher.run(birthdayBatchConfig.birthdayCouponJob(), jobParameters);
	// 	} catch (Exception e) {
	// 		throw new RuntimeException(e.getMessage(), e);
	// 	}
	// }

	@Scheduled(cron = "0 1 0 1 * *")
	public void runBirthCouponJob() {
		try {
			int currentMonth = LocalDate.now().getMonthValue(); // 현재 월을 가져옵니다.

			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("CouponJobTime", System.currentTimeMillis())
				.addString("month", String.valueOf(currentMonth)) // month 파라미터 추가
				.toJobParameters();

			jobLauncher.run(birthdayBatchConfig.birthdayCouponJob(), jobParameters);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}