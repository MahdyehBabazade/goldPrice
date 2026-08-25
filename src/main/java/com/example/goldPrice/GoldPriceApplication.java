package com.example.goldPrice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class GoldPriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldPriceApplication.class, args);
	}
	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}
}
