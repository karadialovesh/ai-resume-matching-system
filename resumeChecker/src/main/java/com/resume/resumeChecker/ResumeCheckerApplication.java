package com.resume.resumeChecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ResumeCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeCheckerApplication.class, args);
	}

}
