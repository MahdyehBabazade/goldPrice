package com.example.goldPrice.service;

import com.example.goldPrice.model.PriceProviders;
import com.example.goldPrice.model.TgjuPrice;
import com.example.goldPrice.repository.PriceProviderRepository;
import com.example.goldPrice.repository.TgjuRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TgjuService {
    private final TgjuRepository tgjuRepository;
    private final PriceProviderRepository priceProviderRepository;



    public TgjuService(TgjuRepository tgjuRepository,
                       PriceProviderRepository priceProviderRepository) {
        this.tgjuRepository = tgjuRepository;
        this.priceProviderRepository = priceProviderRepository;
    }

    @CacheEvict(value = {"tgjuPrice", "finalGoldPrice"}, allEntries = true)
    public void updateRecord(Double tgjuP) {

        //Double tgjuP = fetchPriceService.fetchTgjuPrice();

        TgjuPrice goldPrices = tgjuRepository.findById(1L)
                .orElseGet(() -> {
                    TgjuPrice newPrices = new TgjuPrice();
                    newPrices.setId(1L);

                    return newPrices;
                });

        if (tgjuP != null && tgjuP > 0) {
            goldPrices.setPrice(tgjuP);
            PriceProviders provider = priceProviderRepository.findByName("tgju")
                    .orElseGet(() -> {
                        PriceProviders p = new PriceProviders();
                        p.setName("tgju");
                        return priceProviderRepository.save(p);
                    });
            goldPrices.setPriceProvider(provider);
        }
        tgjuRepository.save(goldPrices);
    }

    @Cacheable(value = "tgjuPrice", key = "'latest'")
    public TgjuPrice getLatestPrice() {
        return tgjuRepository.findById(1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tgju price record not found"));
    }
}