package com.nhnacademy.nuribooksbatch.member.grade.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.nuribooksbatch.member.grade.dto.MemberGradeBatchDto;

@FeignClient(name = "grade", url = "http://localhost:8080")
public interface GradeServiceClient {

	@GetMapping("/api/members/grades/batch-list")
	ResponseEntity<List<MemberGradeBatchDto>> getMemberGradeBatchListByRequirement();
}
