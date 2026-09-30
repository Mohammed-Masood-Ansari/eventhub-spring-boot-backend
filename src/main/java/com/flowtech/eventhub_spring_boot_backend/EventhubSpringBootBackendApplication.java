package com.flowtech.eventhub_spring_boot_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class EventhubSpringBootBackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext=SpringApplication.run(EventhubSpringBootBackendApplication.class, args);
	
		ConfigurableEnvironment environment=applicationContext.getEnvironment();
		
		System.out.println("Event-Hub is running on port = "+environment.getProperty("server.port"));
	}

}
