package com.example.goldPrice.service;

import com.example.goldPrice.model.PriceProviders;
import com.example.goldPrice.model.TalaseaPrice;
import com.example.goldPrice.repository.PriceProviderRepository;
import com.example.goldPrice.repository.TalaseaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TalaseaService {
    private final TalaseaRepository talaseaRepository;
    private final PriceProviderRepository priceProviderRepository;

    public TalaseaService(TalaseaRepository talaseaRepository,
                          PriceProviderRepository priceProviderRepository) {
        this.talaseaRepository = talaseaRepository;
        this.priceProviderRepository = priceProviderRepository;
    }

    @CacheEvict(value = {"talaseaPrice", "finalGoldPrice"}, allEntries = true)
    public void updateRecord(Double talaseaP) {

        //Double talaseaP = fetchPriceService.fetchTalaseaPrice();

        TalaseaPrice goldPrices = talaseaRepository.findById(1L)
                .orElseGet(() -> {
                    TalaseaPrice newPrices = new TalaseaPrice();
                    newPrices.setId(1L);
                    return newPrices;
                });

        if (talaseaP != null && talaseaP > 0) {
            goldPrices.setPrice(talaseaP);

            PriceProviders provider = priceProviderRepository.findByName("talasea")
                    .orElseGet(() -> {
                        PriceProviders p = new PriceProviders();
                        p.setName("talasea");
                        return priceProviderRepository.save(p);
                    });
            goldPrices.setPriceProvider(provider);
        }
        talaseaRepository.save(goldPrices);
    }

    @Cacheable(value = "talaseaPrice", key = "'latest'")
    public TalaseaPrice getLatestPrice() {
        return talaseaRepository.findById(1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Talasea price record not found"));
    }
}