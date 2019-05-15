package com.scor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.scor.dataProcessing.sparkConnection.Connection;

import java.io.Serializable;

import javax.annotation.PreDestroy;


@SpringBootApplication(scanBasePackages={"com.scor"})
@EnableScheduling
public class EAApplication extends SpringBootServletInitializer  implements Serializable {

	private static final long serialVersionUID = -4488854112390651545L;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EAApplication.class);
	}
	
	@PreDestroy
	public void preDestroy() {
		Connection.getContext().close();
		Connection.getSession().close();
	}
 
	public static void main(String[] args) {
		SpringApplication.run(EAApplication.class, args);
	}

}
