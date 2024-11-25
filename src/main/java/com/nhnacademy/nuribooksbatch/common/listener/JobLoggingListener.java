package com.nhnacademy.nuribooksbatch.common.listener;

import java.time.ZoneId;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.common.sender.MessageRequest;
import com.nhnacademy.nuribooksbatch.common.sender.MessageSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobLoggingListener {

	private final MessageSender messageSender;

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		log.info("{}이 시작되었습니다.", jobExecution.getJobInstance().getJobName());
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
			log.error("{} BatchStatus : {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
			messageSender.sendMessage(new MessageRequest(
				"누리북스 배치 알림봇", jobExecution.getJobInstance().getJobName()
				+ "의 BatchStatus가 " + jobExecution.getStatus() + "입니다. 관리자는 해당 작업을 확인하십시오."));
		}

		long jobDuration = jobExecution.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
			- jobExecution.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		log.info("{}이 종료되었습니다.", jobExecution.getJobInstance().getJobName());
		log.info("총 소요시간 : {}ms", jobDuration);
	}
}

