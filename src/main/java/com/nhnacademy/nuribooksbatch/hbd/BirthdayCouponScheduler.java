package com.nhnacademy.nuribooksbatch.hbd;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BirthdayCouponScheduler {

	// 매월 1일 0시 1분에 실행
	@Scheduled(cron = "1 0 0 1 * ?")
	public void execute() {
		// 배치 작업을 호출하여 생일 쿠폰 발급을 처리합니다.
		System.out.println("생일 쿠폰 발급 배치 작업 시작");
	}
}

