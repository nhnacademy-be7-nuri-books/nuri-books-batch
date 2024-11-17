package com.nhnacademy.nuribooksbatch.hbd.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.nuribooksbatch.hbd.BirthdayCouponProcessor;
import com.nhnacademy.nuribooksbatch.hbd.BirthdayCouponWriter;
import com.nhnacademy.nuribooksbatch.hbd.MemberItemReader;
import com.nhnacademy.nuribooksbatch.hbd.entity.Member;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public Job birthdayCouponJob(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("birthdayCouponJob")
			.incrementer(new RunIdIncrementer())
			.flow(step)
			.end()
			.build();
	}

	@Bean
	public Step birthdayCouponStep(StepBuilderFactory stepBuilderFactory, MemberItemReader reader,
		BirthdayCouponProcessor processor, BirthdayCouponWriter writer) {
		return stepBuilderFactory.get("birthdayCouponStep")
			.<Member, Member>chunk(10)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
}
