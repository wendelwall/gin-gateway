package com.riskeys.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.riskeys"})
@EnableScheduling
public class GinGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GinGatewayApplication.class, args);
	}

}
