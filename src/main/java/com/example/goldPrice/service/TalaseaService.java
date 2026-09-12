package com.example.goldPrice.service;

import com.example.goldPrice.model.PriceProviders;
import com.example.goldPrice.model.TalaseaPrice;
import com.example.goldPrice.repository.PriceProviderRepository;
import com.example.goldPrice.repository.StreamRepository;
import com.example.goldPrice.repository.TalaseaRepository;
import com.example.goldPrice.repository.TimeSeriesRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class TalaseaService {
    private final TalaseaRepository talaseaRepository;
    private final PriceProviderRepository priceProviderRepository;
    private final StreamRepository streamRepository;
    private final TimeSeriesRepository timeSeriesRepository;

    public TalaseaService(TalaseaRepository talaseaRepository,
                          PriceProviderRepository priceProviderRepository,
                          StreamRepository streamRepository,
                          TimeSeriesRepository timeSeriesRepository) {
        this.talaseaRepository = talaseaRepository;
        this.priceProviderRepository = priceProviderRepository;
        this.streamRepository = streamRepository;
        this.timeSeriesRepository = timeSeriesRepository;
    }

    @CacheEvict(value = {"talaseaPrice", "finalGoldPrice"}, allEntries = true)
    public void updateRecord(Double talaseaP, String timeFetched) {

        TalaseaPrice goldPrice = talaseaRepository.findById(1L)
                .orElseGet(() -> {
                    TalaseaPrice newPrices = new TalaseaPrice();
                    newPrices.setId(1L);
                    return newPrices;
                });

        if (talaseaP != null && talaseaP > 0) {
            goldPrice.setPrice(talaseaP);
            goldPrice.setFetchedAt(timeFetched);
            PriceProviders provider = priceProviderRepository.findByName("talasea")
                    .orElseGet(() -> {
                        PriceProviders p = new PriceProviders();
                        p.setName("talasea");
                        return priceProviderRepository.save(p);
                    });

            goldPrice.setPriceProvider(provider);
            streamRepository.addToStream("price_stream", String.valueOf(goldPrice.getPrice()),
                    goldPrice.getFetchedAt(), goldPrice.getPriceProvider().getName());
            timeSeriesRepository.addToTimeSeries("price_ts:talasea", goldPrice.getPrice());
        }
        talaseaRepository.save(goldPrice);
    }

    @Cacheable(value = "talaseaPrice", key = "'latest'")
    public double getLatestPrice() {
        Map<String, String> res= streamRepository.readLatestPrice("price_stream", "talasea");
        return Double.parseDouble(res.get("price"));
    }
}