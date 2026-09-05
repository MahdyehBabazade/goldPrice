package com.example.goldPrice.service;

import com.example.goldPrice.model.TalaseaPrice;
import com.example.goldPrice.model.TgjuPrice;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CalculateFinalPriceService {
    private final TalaseaService talaseaService;
    private final TgjuService tgjuService;

    private double tgjuW = 1.0;
    private double talaseaW = 1.0;

    public CalculateFinalPriceService(TalaseaService talaseaService, TgjuService tgjuService){
        this.talaseaService = talaseaService;
        this.tgjuService = tgjuService;
    }

    public Map<String, Double> getCurrentW() {
        return Map.of(
                "TGJU", this.tgjuW,
                "TALASEA", this.talaseaW
        );
    }

    @Cacheable(value = "finalGoldPrice", key = "'calculated'")
    public double calcGoldPrice() {
        TalaseaPrice currentTalaseaPrice = talaseaService.getLatestPrice();
        TgjuPrice currentTgjuPrice = tgjuService.getLatestPrice();
        if (currentTalaseaPrice == null || currentTgjuPrice == null) {
            return 0.0;
        }

        double tgjuP = currentTgjuPrice.getPrice();
        double talaseaP = currentTalaseaPrice.getPrice();
        return (tgjuW * tgjuP + talaseaW * talaseaP) / (tgjuW + talaseaW);
    }

    @CacheEvict(value = "finalGoldPrice", allEntries = true)
    public void updateWeights(Map<String, Double> newWeights) {
        if (newWeights == null) return;

        if (newWeights.containsKey("TGJU") && newWeights.get("TGJU") != null) {
            this.tgjuW = newWeights.get("TGJU");
        }
        if (newWeights.containsKey("TALASEA") && newWeights.get("TALASEA") != null) {
            this.talaseaW = newWeights.get("TALASEA");
        }
    }
}
