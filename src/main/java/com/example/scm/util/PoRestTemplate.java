package com.example.scm.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PoRestTemplate {
	
	
	 @Value("${spring.mail.host}")
	    private String host;

	    @Value("${spring.mail.port}")
	    private int port;

	    @Value("${spring.mail.username}")
	    private String username;

	    @Value("${spring.mail.password}")
	    private String password;

	    @Value("${spring.mail.properties.mail.smtp.auth}")
	    private boolean auth;

	    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	    private boolean starttlsEnable;
	

	@Bean
	  RestTemplate getRestTemplate() {
		return new RestTemplate();
		
		
	}

	 @Bean
	    public JavaMailSender javaMailSender() {
	        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	        mailSender.setHost(host);
	        mailSender.setPort(port);
	        mailSender.setUsername(username);
	        mailSender.setPassword(password);

	        Properties props = mailSender.getJavaMailProperties();
	        props.put("mail.smtp.auth", auth);
	        props.put("mail.smtp.starttls.enable", starttlsEnable);

	        return mailSender;
	    }
	 

}

