package com.jehan.newsBriefService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NewsBriefServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsBriefServiceApplication.class, args);
	}

}
