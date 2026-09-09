package com.example.goldPrice.service;

import com.example.goldPrice.model.PriceProviders;
import com.example.goldPrice.model.TalaseaPrice;
import com.example.goldPrice.model.TgjuPrice;
import com.example.goldPrice.repository.PriceProviderRepository;
import com.example.goldPrice.repository.StreamRepository;
import com.example.goldPrice.repository.TgjuRepository;
import com.example.goldPrice.repository.TimeSeriesRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class TgjuService {
    private final TgjuRepository tgjuRepository;
    private final PriceProviderRepository priceProviderRepository;
    private final StreamRepository streamRepository;
    private final TimeSeriesRepository timeSeriesRepository;

    public TgjuService(TgjuRepository tgjuRepository,
                       PriceProviderRepository priceProviderRepository,
                       StreamRepository streamRepository,
                       TimeSeriesRepository timeSeriesRepository) {
        this.tgjuRepository = tgjuRepository;
        this.priceProviderRepository = priceProviderRepository;
        this.streamRepository = streamRepository;
        this.timeSeriesRepository = timeSeriesRepository;
    }

    @CacheEvict(value = {"tgjuPrice", "finalGoldPrice"}, allEntries = true)
    public void updateRecord(Double tgjuP, String timeFetched) {

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
            timeSeriesRepository.addToTimeSeries("price_ts:tgju", goldPrice.getPrice());

        }
        tgjuRepository.save(goldPrice);
    }

    @Cacheable(value = "tgjuPrice", key = "'latest'")
    public TgjuPrice getLatestPrice() {
        Map<String, String> res= streamRepository.readLatestPrice("price_stream", "tgju");
        TgjuPrice tgjuPrice = new TgjuPrice();
        tgjuPrice.setPrice(Double.parseDouble(res.get("price")));
        tgjuPrice.setFetchedAt(res.get("fetchedAt"));
        PriceProviders provider = new PriceProviders();
        provider.setName(res.get("providerName"));
        tgjuPrice.setPriceProvider(provider);
        return tgjuPrice;
    }
}