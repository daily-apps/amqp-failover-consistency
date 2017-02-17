package com.guideapps.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.guideapps.reports.*")
public class ReportsApplication {
	
    public static void main( String[] args ) {
        SpringApplication.run(ReportsApplication.class, args);
    }
}
