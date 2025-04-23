package com.example.yamyam16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Yamyam16Application {

	public static void main(String[] args) {
		SpringApplication.run(Yamyam16Application.class, args);
	}

}
