package com.scor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.scor" }, exclude = JmsAutoConfiguration.class)
@EnableScheduling
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
public class EAApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EAApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EAApplication.class, args);
	}
}
