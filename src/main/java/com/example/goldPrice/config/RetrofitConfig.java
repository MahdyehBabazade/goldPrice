package com.example.goldPrice.config;

import com.example.goldPrice.client.PriceApiClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {
    @Bean
    public PriceApiClient priceApiClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request requestWithAuth = chain.request().newBuilder()
                            .header("Authorization", "Bearer token123456")
                            .build();
                    return chain.proceed(requestWithAuth);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.talasea.ir/")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(PriceApiClient.class);
    }
}
