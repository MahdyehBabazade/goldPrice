package com.example.goldPrice.client.interceptor;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class AuthInterceptor implements ClientHttpRequestInterceptor {
    private final String authToken;

    public AuthInterceptor(String authToken) {
        this.authToken = authToken;
    }
    @Override
    public @NonNull ClientHttpResponse intercept(HttpRequest request, byte @NonNull [] body,
                                                 ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization",
                "Bearer " + authToken);

        return execution.execute(request, body);
    }
}
