package it.cab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RistoranteDaAzzurroApplication {

	public static void main(String[] args) {
		SpringApplication.run(RistoranteDaAzzurroApplication.class, args);
	}

}
