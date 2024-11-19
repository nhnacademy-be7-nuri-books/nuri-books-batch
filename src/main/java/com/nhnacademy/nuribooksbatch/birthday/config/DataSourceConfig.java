package com.nhnacademy.nuribooksbatch.birthday.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	@Bean(name = "businessDataSourceProperties")
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties businessDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "businessDataSource")
	@Primary
	public DataSource businessDataSource(@Qualifier("businessDataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "batchDataSourceProperties")
	@ConfigurationProperties("spring.datasource-meta")
	public DataSourceProperties batchDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "batchDataSource")
	public DataSource batchDataSource(@Qualifier("batchDataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}
}

