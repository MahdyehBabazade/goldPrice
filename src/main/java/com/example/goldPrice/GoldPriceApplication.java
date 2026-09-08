package com.example.goldPrice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GoldPriceApplication {

	static void main(String[] args) {
		SpringApplication.run(GoldPriceApplication.class, args);
	}

}
