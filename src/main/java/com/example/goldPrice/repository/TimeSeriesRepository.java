package com.example.goldPrice.repository;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.timeseries.AggregationType;
import redis.clients.jedis.timeseries.TSElement;

@Repository
public class TimeSeriesRepository {
    private final JedisPooled jedis;

    public TimeSeriesRepository(JedisPooled jedis) {
        this.jedis = jedis;
    }

    public long addToTimeSeries(String key, double value) {
        return jedis.tsAdd(key, value);
    }

    public void createTSIfNotExist(String key) {
        try {
            jedis.tsCreate(key);
        } catch (JedisDataException e) {
            if (!e.getMessage().contains("already exists")) {
                throw e;
            }
        }
    }

    public void createRuleIfNotExist(String key1, String key2) {
        try {
            jedis.tsCreateRule(key1, key2, AggregationType.AVG, 3600000);
        } catch (JedisDataException e) {
            if (!e.getMessage().contains("already has a src rule")) {
                throw e;
            }
        }
    }
}
