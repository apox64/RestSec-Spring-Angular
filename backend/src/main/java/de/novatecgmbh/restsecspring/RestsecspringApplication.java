package de.novatecgmbh.restsecspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class RestsecspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestsecspringApplication.class, args);
	}
}
