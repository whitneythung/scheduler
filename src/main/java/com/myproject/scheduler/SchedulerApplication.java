package com.myproject.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
@SpringBootApplication adds @Configuration, @EnableAutoConfiguration, and @ComponentScan.
@EnableScheduling ensures that a background task executor is crated. Without it, nothing gets scheduled.
 */
@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}
}
