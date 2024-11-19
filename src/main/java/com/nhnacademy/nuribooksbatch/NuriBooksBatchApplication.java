package com.nhnacademy.nuribooksbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NuriBooksBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(NuriBooksBatchApplication.class, args);
	}

}
