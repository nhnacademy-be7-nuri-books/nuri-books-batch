package com.nhnacademy.nuribooksbatch.hbd;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;

import com.nhnacademy.nuribooksbatch.hbd.entity.Member;

import jakarta.persistence.EntityManagerFactory;

@Component
public class MemberItemReader extends JpaPagingItemReader<Member> {

	@Autowired
	public MemberItemReader(EntityManagerFactory entityManagerFactory) {
		setEntityManagerFactory(entityManagerFactory);
		setQueryString("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.birthday) = :currentMonth");
	}

	@Override
	public void setParameterValues(Map<String, Object> parameters) {
		parameters.put("currentMonth", LocalDate.now().getMonthValue());
	}
}
