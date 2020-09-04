package com.car.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableAutoConfiguration
@EnableDiscoveryClient
public class UserServer {
	public static void main(String[] args) {
		System.setProperty("spring.config.name", "user-server");
		SpringApplication.run(UserServer.class, args);
	}
}
