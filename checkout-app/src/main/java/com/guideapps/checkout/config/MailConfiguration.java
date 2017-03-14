package com.guideapps.checkout.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Profile("!test")
@Configuration
public class MailConfiguration {

	@Value("${mail.host}")
	private String host;
	
	@Value("${mail.port}")
	private String port;
	
	@Bean("javaMailCheckout")
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(host);
		sender.setPort(Integer.valueOf(port));
		return sender;
	}
	
}
