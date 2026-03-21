package com.graduation.userpassport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.graduation,com.graduation.common.jwt")
public class UserpassportApplication {

	public static void main(String[] args) {
		System.out.println("welcome to user passport version 1.0.1");
		SpringApplication.run(UserpassportApplication.class, args);
	}

}
