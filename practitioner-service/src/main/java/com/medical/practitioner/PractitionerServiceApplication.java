package com.medical.practitioner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PractitionerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PractitionerServiceApplication.class, args);
	}
}