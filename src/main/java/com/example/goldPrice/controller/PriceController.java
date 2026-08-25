package com.example.goldPrice.controller;

import com.example.goldPrice.model.Prices;
import com.example.goldPrice.service.PriceService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gold")
public class PriceController {
    private final PriceService service;

    public PriceController(PriceService service){
        this.service = service;
    }

    @PutMapping("/weights")
    public Map<String, Object> updateWeights(@RequestBody Map<String, Double> weights) {
        service.updateWeights(weights);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "weights successfully updated");
        response.put("currentWeights", service.getCurrentW());
        return response;
    }

    @GetMapping("/weights")
    public Map<String, Double> getCurrentWeights() {
        return service.getCurrentW();
    }

    @GetMapping("/calculated-price")
    public Map<String, Object> getFinalPrice() {
        Prices updatedPrices = service.updateRecord();

        Map<String, Object> response = new HashMap<>();
        response.put("rawPrices", updatedPrices);
        response.put("weights", service.getCurrentW());
        response.put("finalPrice", service.calcGoldPrice());
        return response;
    }
}