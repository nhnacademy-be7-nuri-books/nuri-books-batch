package com.nhnacademy.nuribooksbatch.birthday.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.nhnacademy.nuribooksbatch.birthday.domain.Member;

public class MemberRowMapper implements RowMapper<Member> {
	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member member = Member.builder()
			.customerId(rs.getLong("customer_id"))
			.build();

		System.out.println("Row " + rowNum + ": " + member);

		return member;
	}
}

