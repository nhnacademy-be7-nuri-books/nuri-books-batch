package com.nhnacademy.nuribooksbatch.common.listener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StepLoggingListener {

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("{}이 시작되었습니다.", stepExecution.getStepName());
	}

	@AfterStep
	public void afterStep(StepExecution stepExecution) {
		if (!stepExecution.getStatus().isUnsuccessful()) {
			log.info("{}에서 읽은 아이템 수: {}, 처리된 아이템 수: {}",
				stepExecution.getStepName(), stepExecution.getReadCount(), stepExecution.getWriteCount());
		}

		log.info("{}이 종료되었습니다.", stepExecution.getStepName());
		log.info("{} batchStatus : {}, exitStatus : {}",
			stepExecution.getStepName(), stepExecution.getStatus(), stepExecution.getExitStatus());
	}
}
