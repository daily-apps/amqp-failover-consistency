package com.guideapps.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.guideapps.checkout.*")
public class CheckoutApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CheckoutApplication.class, args);
	}
}
