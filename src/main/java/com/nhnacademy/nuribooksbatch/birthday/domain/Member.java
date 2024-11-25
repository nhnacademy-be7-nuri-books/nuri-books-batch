package com.nhnacademy.nuribooksbatch.birthday.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private Long customer_id;

	@Override
	public String toString() {
		return "member_id= " + this.customer_id;
	}
}
