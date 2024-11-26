package com.nhnacademy.nuribooksbatch.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.common.sender.MessageRequest;
import com.nhnacademy.nuribooksbatch.common.sender.MessageSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BatchControllerAspect {

	private final MessageSender messageSender;

	// 포인트컷 정의 - BatchController에서 발생하는 예외를 처리
	@Pointcut("execution(* com.nhnacademy.nuribooksbatch.member.BatchController.*(..))")
	public void batchJobMethods() {
	}

	// 예외가 발생할 경우 호출되는 메소드
	@AfterThrowing(pointcut = "batchJobMethods()", throwing = "exception")
	public void handleJobExecutionException(JoinPoint joinPoint, Throwable exception) {
		// 예외 로깅
		log.error("수동으로 작동시킨 배치 작업에서 에러가 발생하였습니다. 메소드: {}. 예외: {}",
			joinPoint.getSignature().toShortString(), exception.getMessage());

		// 메시지 전송
		messageSender.sendMessage(new MessageRequest(
			"누리북스 배치 알림봇",
			"수동으로 작동시킨 배치 작업에서 에러가 발생하였습니다. "
				+ "메소드: " + joinPoint.getSignature().toShortString() + ", 예외: " + exception.getMessage()
		));
	}

	// 배치 작업이 성공적으로 종료된 후 실행 (배치 성공 메시지)
	@AfterReturning(pointcut = "batchJobMethods()", returning = "result")
	public void handleJobSuccess(JoinPoint joinPoint, Object result) {
		log.info("수동으로 작동시킨 배치 작업이 성공적으로 완료되었습니다. 작업명: {}", joinPoint.getSignature().getName());

		// 배치 성공 메시지 전송
		messageSender.sendMessage(new MessageRequest(
			"누리북스 배치 알림봇",
			"수동으로 작동시킨 배치 작업이 성공적으로 완료되었습니다. 작업명: " + joinPoint.getSignature().getName()
		));
	}
}
