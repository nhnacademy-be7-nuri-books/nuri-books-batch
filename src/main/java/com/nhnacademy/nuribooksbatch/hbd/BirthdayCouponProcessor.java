package com.nhnacademy.nuribooksbatch.hbd;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.hbd.entity.Member;

@Component
public class BirthdayCouponProcessor implements ItemProcessor<Member, Member> {

	@Override
	public Member process(Member member) throws Exception {
		// 생일 쿠폰 발급 로직을 추가할 수 있습니다.
		System.out.println("생일 쿠폰 발급: " + member.getName());
		// 쿠폰 발급 처리 후, 처리된 멤버 객체를 리턴합니다.
		return member;
	}
}

