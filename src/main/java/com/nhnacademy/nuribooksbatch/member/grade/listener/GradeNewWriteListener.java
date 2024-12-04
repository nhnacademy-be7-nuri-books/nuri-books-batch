package com.nhnacademy.nuribooksbatch.member.grade.listener;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.member.grade.dto.MemberGradeBatchDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GradeNewWriteListener {

	@BeforeWrite
	void beforeWrite(Chunk<? extends MemberGradeBatchDto> items) {
	}

	@AfterWrite
	void afterWrite(Chunk<? extends MemberGradeBatchDto> items) {
		items.forEach(memberGradeBatchDto ->
			log.info("등급이 업데이트된 customerId : {}", memberGradeBatchDto.getCustomerId()));
	}
}
