package com.example.goldPrice.config;

import redis.clients.jedis.RedisClient;

public class RedisConfig {
    private static final RedisClient jedis = RedisClient.create
            ("redis://localhost:6379");

    public static RedisClient getJedis() {
        return jedis;
    }
}
