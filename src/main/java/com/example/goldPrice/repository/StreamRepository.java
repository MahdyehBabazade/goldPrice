package com.example.goldPrice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.StreamEntryID;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StreamRepository {

    private final JedisPooled jedis;
    private final ObjectMapper objectMapper;

    public StreamRepository(JedisPooled jedis, ObjectMapper objectMapper) {
        this.jedis = jedis;
        this.objectMapper = objectMapper;
    }

    public String addToStream(String key, Object object) {
        try {
            String jsonToString = objectMapper.writeValueAsString(object);
            Map<String, String> streamData = new HashMap<>();
            streamData.put("data", jsonToString);

            StreamEntryID id = jedis.xadd(key, StreamEntryID.NEW_ENTRY, streamData);
            return id.toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error converting to JSON", e);
        }
    }
}