package com.devsu.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eurekaserver3Application {

	public static void main(String[] args) {
		SpringApplication.run(Eurekaserver3Application.class, args);
	}

}
