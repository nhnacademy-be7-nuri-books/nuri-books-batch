package com.nhnacademy.nuribooksbatch.member.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoginScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 30 0 * * *", zone = "Asia/Seoul")
    public void runInactiveMembersByLastLoginJob() {

        try {
            log.info("회원의 최근 로그인 일시를 확인하는 작업이 시작되었습니다 : {}", LocalDateTime.now());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
            String date = dateFormat.format(new Date());

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("memberStatusUpdateDate", date)
                    .toJobParameters();

            jobLauncher.run(jobRegistry.getJob("inactiveMembersByLastLoginJob"), jobParameters);
        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                JobParametersInvalidException | JobRestartException e) {
            log.error("스케줄러 에러 : 회원의 최근 로그인 일시를 확인하는 작업에 문제가 생겼습니다 : {}", e.getMessage());
        }
    }
}
