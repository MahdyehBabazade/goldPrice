package com.example.goldPrice.service;

import com.example.goldPrice.model.Prices;
import com.example.goldPrice.repository.PriceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class PriceService {
    private final PriceRepository priceRepository;
    private final FetchPriceService fetchPriceService;

    private double tgjuW = 1.0;
    private double talaseaW = 1.0;

    public PriceService(PriceRepository priceRepository, FetchPriceService fetchPriceService) {
        this.priceRepository = priceRepository;
        this.fetchPriceService = fetchPriceService;
    }

    public Prices updateRecord() {

        Double tgjuP = fetchPriceService.fetchTgjuPrice();
        Double talaseaP = fetchPriceService.fetchTalaseaPrice();

        Prices goldPrices = priceRepository.findById(1L)
                .orElseGet(() -> {
                    Prices newPrices = new Prices();
                    newPrices.setId(1L);
                    return newPrices;
                });

        if (tgjuP != null && tgjuP > 0) {
            goldPrices.setTgju(tgjuP);
        }
        if (talaseaP != null && talaseaP > 0) {
            goldPrices.setTalasea(talaseaP);
        }
        return priceRepository.save(goldPrices);
    }

    public void updateWeights(Map<String, Double> newWeights) {
        if (newWeights == null) return;

        if (newWeights.containsKey("TGJU") && newWeights.get("TGJU") != null) {
            this.tgjuW = newWeights.get("TGJU");
        }
        if (newWeights.containsKey("TALASEA") && newWeights.get("TALASEA") != null) {
            this.talaseaW = newWeights.get("TALASEA");
        }
    }

    public double calcGoldPrice() {
        Prices currentPrices = getLatestPrice();
        if (currentPrices == null) {
            return 0.0;
        }

        double tgjuP = currentPrices.getTgju();
        double talaseaP = currentPrices.getTalasea();

        return (tgjuW * tgjuP + talaseaW * talaseaP) / (tgjuW + talaseaW);
    }

    public Prices getLatestPrice() {
        return priceRepository.findById(1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prices record not found"));
    }

    public Map<String, Double> getCurrentW() {
        return Map.of(
                "TGJU", this.tgjuW,
                "TALASEA", this.talaseaW
        );
    }


}