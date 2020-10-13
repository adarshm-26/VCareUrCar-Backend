package com.car.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReportsApplication {
  public static void main(String[] args) {
    SpringApplication.run(ReportsApplication.class, args);
  }
}
