package com.example.goldPrice.service;

import com.example.goldPrice.model.PriceProviders;
import com.example.goldPrice.model.TgjuPrice;
import com.example.goldPrice.repository.PriceProviderRepository;
import com.example.goldPrice.repository.StreamRepository;
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
    private final StreamRepository streamRepository;



    public TgjuService(TgjuRepository tgjuRepository,
                       PriceProviderRepository priceProviderRepository,
                       StreamRepository streamRepository) {
        this.tgjuRepository = tgjuRepository;
        this.priceProviderRepository = priceProviderRepository;
        this.streamRepository = streamRepository;
    }

    @CacheEvict(value = {"tgjuPrice", "finalGoldPrice"}, allEntries = true)
    public void updateRecord(Double tgjuP, String timeFetched) {

        //Double tgjuP = fetchPriceService.fetchTgjuPrice();

        TgjuPrice goldPrice = tgjuRepository.findById(1L)
                .orElseGet(() -> {
                    TgjuPrice newPrices = new TgjuPrice();
                    newPrices.setId(1L);

                    return newPrices;
                });

        if (tgjuP != null && tgjuP > 0) {
            goldPrice.setPrice(tgjuP);
            goldPrice.setFetchedAt(timeFetched);
            PriceProviders provider = priceProviderRepository.findByName("tgju")
                    .orElseGet(() -> {
                        PriceProviders p = new PriceProviders();
                        p.setName("tgju");
                        return priceProviderRepository.save(p);
                    });
            goldPrice.setPriceProvider(provider);
            streamRepository.addToStream("price_stream", String.valueOf(goldPrice.getPrice()),
                    goldPrice.getFetchedAt(), goldPrice.getPriceProvider().getName());
        }
        tgjuRepository.save(goldPrice);


    }

    @Cacheable(value = "tgjuPrice", key = "'latest'")
    public TgjuPrice getLatestPrice() {
        return tgjuRepository.findById(1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tgju price record not found"));
    }
}