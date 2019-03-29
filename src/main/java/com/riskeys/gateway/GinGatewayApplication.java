package com.riskeys.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GinGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GinGatewayApplication.class, args);
	}

}
