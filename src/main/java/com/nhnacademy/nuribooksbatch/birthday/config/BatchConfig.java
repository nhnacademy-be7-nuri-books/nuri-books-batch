package com.nhnacademy.nuribooksbatch.birthday.config;

import javax.sql.DataSource;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

	private final DataSource batchDataSource;

	public BatchConfig(@Qualifier("batchDataSource") DataSource batchDataSource) {
		this.batchDataSource = batchDataSource;
	}

	@Bean
	public PlatformTransactionManager batchTransactionManager() {
		return new DataSourceTransactionManager(batchDataSource);
	}

	@Bean(name = "customJobRepository")
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(batchDataSource);
		factory.setTransactionManager(batchTransactionManager());
		factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
		factory.setDatabaseType("MYSQL");

		factory.afterPropertiesSet();
		return factory.getObject();
	}
}
