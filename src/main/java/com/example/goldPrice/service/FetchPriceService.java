package com.example.goldPrice.service;

import com.example.goldPrice.repository.PriceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FetchPriceService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public FetchPriceService(RestClient restClient) {
        this.restClient = restClient;
        this.objectMapper = new ObjectMapper();

    }
    public Double fetchTgjuPrice() {
        try {
            String jsonResponse = restClient.get()
                    .uri("https://call.tgju.org/ajax.json")
                    .retrieve()
                    .body(String.class);

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode current = root.get("current");
                if (current != null && current.has("geram18")) {
                    JsonNode geram18Node = current.get("geram18");
                    if (geram18Node.has("p")) {
                        String rawPrice = geram18Node.get("p").asText();
                        return Double.parseDouble(normalizeNumber(rawPrice))/10000.0;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching gold price from TGJU: " + e.getMessage());
        }
        return null;
    }

    public Double fetchTalaseaPrice() {
        try {
            String jsonResponse = restClient.get()
                    .uri("https://api.talasea.ir/api/market/getGoldPrice")
                    .retrieve()
                    .body(String.class);

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                JsonNode root = objectMapper.readTree(jsonResponse);
                if (root.has("price")) {
                    String rawPrice = root.get("price").asText();
                    return Double.parseDouble(normalizeNumber(rawPrice));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching gold price from TALASEA: " + e.getMessage());
        }
        return null;
    }

    private String normalizeNumber(String input) {
        if (input == null || input.isEmpty()) return "0";
        char[] chars = input.toCharArray();
        return new String(chars).replaceAll("[^0-9.]", "");
    }
}
