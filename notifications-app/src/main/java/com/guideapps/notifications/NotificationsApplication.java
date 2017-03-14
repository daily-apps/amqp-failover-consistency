package com.guideapps.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.guideapps.notifications.*")
public class NotificationsApplication {
	
    public static void main( String[] args ) {
    	SpringApplication.run(NotificationsApplication.class, args);
    }
}
