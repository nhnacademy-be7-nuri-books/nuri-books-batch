package com.nhnacademy.nuribooksbatch.hbd;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

import com.nhnacademy.nuribooksbatch.hbd.entity.Member;

@Component
public class BirthdayCouponWriter implements ItemWriter<Member> {

	@Override
	public void write(List<? extends Member> items) throws Exception {
		// 생일 쿠폰 발급 로직을 구현
		for (Member member : items) {
			// 예시로 쿠폰 발급을 콘솔로 출력
			System.out.println("쿠폰 발급 완료: " + member.getName());
			// 실제로는 쿠폰을 DB에 저장하거나 이메일을 전송하는 등의 작업을 합니다.
		}
	}

	@Override
	public void write(Chunk<? extends Member> chunk) throws Exception {
		// 생일 쿠폰 발급 로직을 구현
		for (Member member : items) {
			// 예시로 쿠폰 발급을 콘솔로 출력
			System.out.println("쿠폰 발급 완료: " + member.getName());
			// 실제로는 쿠폰을 DB에 저장하거나 이메일을 전송하는 등의 작업을 합니다.
		}
	}
}
