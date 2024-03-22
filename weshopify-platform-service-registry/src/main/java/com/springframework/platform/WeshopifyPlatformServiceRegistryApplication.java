package com.springframework.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WeshopifyPlatformServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshopifyPlatformServiceRegistryApplication.class, args);
	}

}
