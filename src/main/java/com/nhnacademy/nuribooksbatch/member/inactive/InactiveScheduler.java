package com.nhnacademy.nuribooksbatch.member.inactive;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.common.sender.MessageRequest;
import com.nhnacademy.nuribooksbatch.common.sender.MessageSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InactiveScheduler {

	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;
	private final MessageSender messageSender;

	@Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
	public void runInactiveMembersByLastLoginJob() {

		try {
			log.info("휴면 회원을 탐색하는 스케줄러가 시작되었습니다.");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
			String formattedDate = LocalDateTime.now().format(formatter);

			JobParameters jobParameters = new JobParametersBuilder()
				.addString("memberStatusUpdateDate", formattedDate)
				.toJobParameters();

			jobLauncher.run(jobRegistry.getJob("inactiveMembersByLastLoginJob"), jobParameters);

		} catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException
				 | JobParametersInvalidException | JobRestartException e) {
			log.error("스케줄러 에러 - 휴면 회원을 탐색하는 작업에 문제가 발생하였습니다. : {}", e.getMessage());
			messageSender.sendMessage(new MessageRequest("누리북스 배치 알림봇",
				"스케줄러 에러 - 휴면 회원을 탐색하는 작업에 문제가 발생하였습니다. "
					+ "관리자는 해당 배치 작업을 확인하십시오. : " + e.getMessage()));
		}
	}
}
