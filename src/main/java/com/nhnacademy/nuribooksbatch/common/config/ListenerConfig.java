package com.nhnacademy.nuribooksbatch.common.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ListenerConfig {

	// Step 전후로 logging하는 listener
	@Bean
	public StepExecutionListener stepLoggingListener() {

		return new StepExecutionListener() {
			@Override
			public void beforeStep(@NonNull StepExecution stepExecution) {
				log.info("배치 작업을 위한 스텝이 시작됩니다. : {}", stepExecution.getStepName());
			}

			@Override
			public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
				log.info("배치 작업을 위한 스텝이 완료되었습니다. : {}", stepExecution.getStepName());
				return ExitStatus.COMPLETED;
			}
		};
	}
}
