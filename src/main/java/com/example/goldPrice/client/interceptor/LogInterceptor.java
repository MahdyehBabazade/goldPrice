package com.example.goldPrice.client.interceptor;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LogInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory
            .getLogger(LogInterceptor.class);

    @Override
    public @NonNull ClientHttpResponse intercept(@NonNull HttpRequest request, byte @NonNull [] body, ClientHttpRequestExecution execution) throws IOException {
        logger.info("REQUEST");
        logger.info("URI: {}", request.getURI());
        logger.info("Method: {}", request.getMethod());
        logger.info("Headers: {}", request.getHeaders());
        if (body.length > 0) {
            logger.info("Request Body: {}", new String(body, StandardCharsets.UTF_8));
        }

        ClientHttpResponse response = execution.execute(request, body);

        logger.info("RESPONSE");
        logger.info("Status code: {}", response.getStatusCode());
        logger.info("Status text: {}", response.getStatusText());
        logger.info("Headers: {}", response.getHeaders());

        String responseBody = readResponseBody(response);
        logger.info("Body: {}", responseBody);

        return response;
    }

    private String readResponseBody(ClientHttpResponse response) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("Failed to read response body", e);
            return "[Error reading body]";
        }
    }
}