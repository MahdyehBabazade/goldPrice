package com.example.goldPrice.controller;

import com.example.goldPrice.model.TalaseaPrice;
import com.example.goldPrice.model.TgjuPrice;
import com.example.goldPrice.service.CalculateFinalPriceService;
import com.example.goldPrice.service.TalaseaService;
import com.example.goldPrice.service.TgjuService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gold")
public class PriceController {
    private final CalculateFinalPriceService calcService;
    private final TgjuService tgjuService;
    private final TalaseaService talaseaService;

    public PriceController(CalculateFinalPriceService calcService, TgjuService tgjuService, TalaseaService talaseaService){
        this.calcService = calcService;
        this.tgjuService = tgjuService;
        this.talaseaService = talaseaService;

    }

    @PutMapping("/weights")
    public Map<String, Object> updateWeights(@RequestBody Map<String, Double> weights) {
        calcService.updateWeights(weights);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "weights successfully updated");
        response.put("currentWeights", calcService.getCurrentW());
        return response;
    }

    @GetMapping("/weights")
    public Map<String, Double> getCurrentWeights() {
        return calcService.getCurrentW();
    }

    @GetMapping("/calculated-price")
    public Map<String, Object> getFinalPrice() {
        TgjuPrice updatedTgjuPrice = tgjuService.getLatestPrice();
        TalaseaPrice updatedTalaseaPrice = talaseaService.getLatestPrice();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rawTgjuPrice", updatedTgjuPrice);
        response.put("rawTalaseaPrice", updatedTalaseaPrice);
        response.put("weights", calcService.getCurrentW());
        response.put("finalPrice", calcService.calcGoldPrice());
        return response;
    }
}