package com.smsmissedcall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmsMissedCallApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsMissedCallApplication.class, args);
	}
}
