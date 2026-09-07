package com.example.goldPrice.config; // پکیج کانفیگ پروژه شما

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    private static JedisPool jedisPool;

    public static JedisPool getPool() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(10);
            jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        }
        return jedisPool;
    }
}