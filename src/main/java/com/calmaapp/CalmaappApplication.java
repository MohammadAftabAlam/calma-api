package com.calmaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.calmaapp")
public class CalmaappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalmaappApplication.class, args);

		
	}

	

}
