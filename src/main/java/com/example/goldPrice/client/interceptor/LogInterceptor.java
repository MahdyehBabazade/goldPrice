package com.example.goldPrice.client.interceptor;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LogInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory
            .getLogger(LogInterceptor.class);

    @Override
    public @NonNull ClientHttpResponse intercept(HttpRequest request, byte @NonNull [] body, ClientHttpRequestExecution execution) throws IOException {
        logger.info("REQUEST");
        logger.info("URI: {}", request.getURI());
        logger.info("Method: {}",  request.getMethod());
        logger.info("Headers: {}", request.getHeaders());

        ClientHttpResponse response = execution.execute(request, body);

        logger.info("RESPONSE");
        logger.info("Body: {}", response.getBody());
        logger.info("Status code: {}", response.getStatusCode());
        logger.info("Status text: {}", response.getStatusText());

        return response;
    }
}
