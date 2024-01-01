package com.domain.gym.gymspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class GymspringApplication {

	public static void main(String[] args) {
		// SpringApplication app = new SpringApplication(GymspringApplication.class);
		// app.addListeners(new ApplicationPidFileWriter());
		// app.setWebApplicationType(WebApplicationType.SERVLET);
		// app.run(args);

		SpringApplication.run(GymspringApplication.class, args);
	}

}
