package com.example.goldPrice.repository;

import com.example.goldPrice.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class StreamRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String addToStream(String key, Object object){
        try(Jedis jedis = RedisConfig.getPool().getResource()){
            String jsonToString = objectMapper.writeValueAsString(object);
            Map<String, String> streamData = new HashMap<>();
            streamData.put("data", jsonToString);
            StreamEntryID id = jedis.xadd(key, StreamEntryID.NEW_ENTRY, streamData);
            return id.toString();
        }
    }
}
