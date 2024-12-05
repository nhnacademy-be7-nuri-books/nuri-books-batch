package com.nhnacademy.nuribooksbatch.birthday.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.nuribooksbatch.birthday.domain.Member;

class MemberRowMapperTest {

	@Mock
	private ResultSet resultSet; // Mocking ResultSet

	private MemberRowMapper memberRowMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		memberRowMapper = new MemberRowMapper();
	}

	@Test
	void testMapRow() throws SQLException {
		// Given
		Long mockCustomerId = 123L;
		when(resultSet.getLong("customer_id")).thenReturn(mockCustomerId);

		// When
		Member member = memberRowMapper.mapRow(resultSet, 1);

		// Then
		assertNotNull(member);
		assertEquals(mockCustomerId, member.getCustomerId());
	}
}