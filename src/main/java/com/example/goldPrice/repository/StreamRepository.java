package com.example.goldPrice.repository;

import com.example.goldPrice.model.PriceProviders;
import com.example.goldPrice.model.TalaseaPrice;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.resps.StreamEntry;

import java.util.*;

@Repository
public class StreamRepository {

    private final JedisPooled jedis;

    public StreamRepository(JedisPooled jedis) {
        this.jedis = jedis;
    }

    public String addToStream(String key, String price, String fetchedAt, String priceProviderName) {

        Map<String, String> streamData = new HashMap<>();
        streamData.put("Price", price);
        streamData.put("fetchedAt", fetchedAt);
        streamData.put("Price Provider", priceProviderName);
        StreamEntryID id = jedis.xadd(key, StreamEntryID.NEW_ENTRY, streamData);
        return id.toString();
    }

    public Map<String, String> readLatestPrice(String key, String provider_name) {
        List<StreamEntry> streamEntries = jedis.xrevrange(key, "+", "-");

        if (streamEntries != null) {
            for (StreamEntry streamEntry : streamEntries) {
                Map<String, String> result = new HashMap<>();
                if (streamEntry.getFields().get("Price Provider").equals(provider_name)) {
                    String providerName = streamEntry.getFields().get("Price Provider");
                    String price = streamEntry.getFields().get("Price");
                    String fetchedAt = streamEntry.getFields().get("fetchedAt");
                    result.put("price", price);
                    result.put("providerName", providerName);
                    result.put("fetchedAt", fetchedAt);
                    return result;
                }
            }
        }
        return Collections.emptyMap();
    }
}