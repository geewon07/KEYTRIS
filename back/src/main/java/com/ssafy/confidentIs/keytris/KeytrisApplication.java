package com.ssafy.confidentIs.keytris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KeytrisApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeytrisApplication.class, args);

	}

}
