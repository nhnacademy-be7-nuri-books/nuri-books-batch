package com.nhnacademy.nuribooksbatch.member.grade.listener;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.member.grade.dto.GradeUpdateCustomerIdDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GradeWriteListener {

	@BeforeWrite
	void beforeWrite(Chunk<? extends GradeUpdateCustomerIdDto> items) {
	}

	@AfterWrite
	void afterWrite(Chunk<? extends GradeUpdateCustomerIdDto> items) {
		items.forEach(gradeUpdateCustomerIdDto ->
			log.info("등급이 업데이트된 customerId : {}", gradeUpdateCustomerIdDto.getCustomer_id()));
	}
}
