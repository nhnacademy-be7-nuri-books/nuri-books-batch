package com.nhnacademy.nuribooksbatch.member.grade.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.nhnacademy.nuribooksbatch.member.grade.dto.MemberGradeBatchDto;
import com.nhnacademy.nuribooksbatch.member.grade.feign.GradeServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberGradeIdNewReader implements ItemReader<MemberGradeBatchDto> {

	private final GradeServiceClient gradeServiceClient;
	private List<MemberGradeBatchDto> batchList;
	private Iterator<MemberGradeBatchDto> iterator;

	@Override
	public MemberGradeBatchDto read() {

		if (batchList == null) {
			batchList = gradeServiceClient.getMemberGradeBatchListByRequirement().getBody();
			iterator = batchList.iterator();
		}

		return iterator.hasNext() ? iterator.next() : null;
	}
}
