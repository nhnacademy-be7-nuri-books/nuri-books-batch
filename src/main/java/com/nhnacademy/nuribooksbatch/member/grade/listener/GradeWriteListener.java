package com.nhnacademy.nuribooksbatch.member.grade.listener;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.member.inactive.dto.InactiveCustomerIdDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GradeWriteListener {

	@BeforeWrite
	void beforeWrite(Chunk<? extends InactiveCustomerIdDto> items) {
	}

	@AfterWrite
	void afterWrite(Chunk<? extends InactiveCustomerIdDto> items) {
		items.forEach(customerIdDto -> log.info("등급이 업데이트된 customerId : {}", customerIdDto.getCustomer_id()));
	}
}
