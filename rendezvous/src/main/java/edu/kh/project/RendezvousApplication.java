package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class RendezvousApplication {

	public static void main(String[] args) {
		SpringApplication.run(RendezvousApplication.class, args);
	}

}
