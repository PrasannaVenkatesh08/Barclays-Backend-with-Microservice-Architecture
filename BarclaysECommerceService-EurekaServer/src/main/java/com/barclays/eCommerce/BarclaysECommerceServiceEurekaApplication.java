package com.barclays.eCommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BarclaysECommerceServiceEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarclaysECommerceServiceEurekaApplication.class, args);
	}

}
