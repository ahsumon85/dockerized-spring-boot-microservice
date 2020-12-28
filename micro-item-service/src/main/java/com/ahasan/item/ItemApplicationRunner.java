package com.ahasan.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;



@SpringBootApplication
@EnableEurekaClient
public class ItemApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ItemApplicationRunner.class, args);
		System.out.println("Product service ruuning....!");
	}

}
