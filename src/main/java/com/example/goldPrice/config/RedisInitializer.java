package com.example.goldPrice.config;

import com.example.goldPrice.repository.TimeSeriesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer implements CommandLineRunner {
    private final TimeSeriesRepository timeSeriesRepository;

    public RedisInitializer(TimeSeriesRepository timeSeriesRepository) {
        this.timeSeriesRepository = timeSeriesRepository;
    }

    @Override
    public void run(String... args) {
        timeSeriesRepository.createTSIfNotExist("price_ts:tgju");
        timeSeriesRepository.createTSIfNotExist("price_ts:talasea");

        timeSeriesRepository.createTSIfNotExist("price_ts:tgju:hourly");
        timeSeriesRepository.createTSIfNotExist("price_ts:talasea:hourly");

        timeSeriesRepository.createRuleIfNotExist("price_ts:tgju", "price_ts:tgju:hourly");
        timeSeriesRepository.createRuleIfNotExist("price_ts:talasea", "price_ts:talasea:hourly");
    }
}
