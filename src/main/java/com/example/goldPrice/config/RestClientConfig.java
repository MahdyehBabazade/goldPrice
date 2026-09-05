package com.example.goldPrice.config;

import com.example.goldPrice.client.interceptor.AuthInterceptor;
import com.example.goldPrice.client.interceptor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://call.tgju.org/")
                .requestInterceptor(new AuthInterceptor("token123456"))
                .requestInterceptor(new LogInterceptor())
                .build();
    }
}
