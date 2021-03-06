package com.allchat.allchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AllchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllchatApplication.class, args);
	}

}
