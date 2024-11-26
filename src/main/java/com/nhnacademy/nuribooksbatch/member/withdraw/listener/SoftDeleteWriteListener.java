package com.nhnacademy.nuribooksbatch.member.withdraw.listener;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.member.withdraw.dto.WithdrawnCustomerIdDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SoftDeleteWriteListener {

	@BeforeWrite
	void beforeWrite(Chunk<? extends WithdrawnCustomerIdDto> items) {
	}

	@AfterWrite
	void afterWrite(Chunk<? extends WithdrawnCustomerIdDto> items) {
		items.forEach(withdrawnCustomerIdDto ->
			log.info("Soft Delete 처리된 customerId : {}", withdrawnCustomerIdDto.getCustomer_id()));
	}
}
