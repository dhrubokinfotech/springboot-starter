package com.disl.boilerplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BoilerplateApplication {
	public static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(BoilerplateApplication.class, args);
	}
}
