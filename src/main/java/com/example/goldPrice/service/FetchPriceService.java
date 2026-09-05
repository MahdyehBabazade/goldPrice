package com.example.goldPrice.service;

import com.example.goldPrice.client.PriceApiClient;
import com.example.goldPrice.dto.TalaseaResponse;
import com.example.goldPrice.dto.TgjuResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import retrofit2.Response;

@Service
public class FetchPriceService {
    private final RestClient restClient;
    private final PriceApiClient priceApiClient;
    private final TgjuService tgjuService;
    private final TalaseaService talaseaService;


    public FetchPriceService(RestClient restClient, PriceApiClient priceApiClient,
                             TgjuService tgjuService, TalaseaService talaseaService) {
        this.restClient = restClient;
        this.priceApiClient = priceApiClient;
        this.tgjuService = tgjuService;
        this.talaseaService = talaseaService;

    }

    @Scheduled(fixedRate = 60000)
    public void fetchTgjuPrice() {
        try {
            TgjuResponse response = restClient.get()
                    .uri("ajax.json")
                    .retrieve()
                    .body(TgjuResponse.class);

            if (response != null && response.current() != null
                    && response.current().geram18() != null) {
                tgjuService.updateRecord(Double.parseDouble(normalizeNumber(response.current()
                        .geram18().price()))/10000.0);
            }
        } catch (Exception e) {
            System.err.println("Error fetching gold price from TGJU: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 60000)
    public void fetchTalaseaPrice() {
        try {
            Response<TalaseaResponse> response= priceApiClient.getTalaseaPrice()
                    .execute();

            if (response.body() != null && response.isSuccessful()) {
                talaseaService.updateRecord(Double.parseDouble(normalizeNumber(response.body().price())));
            }
        } catch (Exception e) {
            System.err.println("Error fetching gold price from TALASEA: " + e.getMessage());
        }
    }

    private String normalizeNumber(String input) {
        if (input == null || input.isEmpty()) return "0";
        char[] chars = input.toCharArray();
        return new String(chars).replaceAll("[^0-9.]", "");
    }
}
