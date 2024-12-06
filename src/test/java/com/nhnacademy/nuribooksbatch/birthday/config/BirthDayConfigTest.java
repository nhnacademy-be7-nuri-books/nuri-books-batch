package com.nhnacademy.nuribooksbatch.birthday.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.nhnacademy.nuribooksbatch.birthday.domain.Member;
import com.nhnacademy.nuribooksbatch.common.sender.MessageSender;

@Configuration
@EnableBatchProcessing
class BirthDayConfigTest {
	@Mock
	private JobLauncher jobLauncher;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private PlatformTransactionManager platformTransactionManager;

	@Mock
	private MessageSender messageSender;

	@Mock
	private Job job;

	@Mock
	private JdbcPagingItemReader<Member> itemReader;

	@Mock
	private ItemWriter<Member> itemWriter;

	@Mock
	private DataSource dataSource;

	@InjectMocks
	private BirthDayConfig birthDayConfig;
	@Mock
	private ResultSet resultSet;

	private MemberRowMapper memberRowMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		itemReader = new JdbcPagingItemReader<>();

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("customer_id", Order.ASCENDING);

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("SELECT customer_id");
		queryProvider.setFromClause("FROM members");
		queryProvider.setSortKeys(sortKeys);

		String currentMonthCondition = "WHERE MONTH(birthday) = MONTH(CURRENT_DATE())";
		queryProvider.setWhereClause(currentMonthCondition);

		itemReader.setDataSource(dataSource);
		itemReader.setPageSize(10);
		itemReader.setQueryProvider(queryProvider);
		itemReader.setRowMapper(new MemberRowMapper());

		memberRowMapper = new MemberRowMapper();

	}

	@Test
	void runJobAtScheduledTime() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {

		JobExecution jobExecution = mock(JobExecution.class);

		when(jobLauncher.run(any(Job.class), any())).thenReturn(jobExecution);

		birthDayConfig.runJobAtScheduledTime();

		verify(jobLauncher, times(1)).run(any(Job.class), any());
	}

	@Test
	void runJobAtScheduledTime_ShouldSendErrorNotification_WhenExceptionOccurs() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {
		when(jobLauncher.run(any(), any())).thenThrow(JobExecutionAlreadyRunningException.class);

		birthDayConfig.runJobAtScheduledTime();

		verify(messageSender, times(1)).sendMessage(any());
	}

	@Test
	void birthdayCouponJob() {
		Step sendCouponStep = birthDayConfig.sendCouponStep();

		Job birthdayCouponJob = birthDayConfig.birthdayCouponJob(sendCouponStep);

		assertNotNull(birthdayCouponJob);

		assertEquals("issueBirthdayCouponByBirthdayCouponJob", birthdayCouponJob.getName());
		assertNotNull(sendCouponStep);
		assertEquals("sendCouponStep", sendCouponStep.getName());
	}

	@Test
	void sendCouponStep() {
		Step step = birthDayConfig.sendCouponStep();

		assertNotNull(step);
		assertEquals("sendCouponStep", step.getName());

		step.getStartLimit();
	}

	@Test
	void mapRow_ShouldMapResultSetToMember() throws SQLException {
		// Given
		when(resultSet.getLong("customer_id")).thenReturn(1L);

		// When
		Member member = memberRowMapper.mapRow(resultSet, 1);

		// Then
		assertNotNull(member);
		assertEquals(1L, member.getCustomerId());
	}
}