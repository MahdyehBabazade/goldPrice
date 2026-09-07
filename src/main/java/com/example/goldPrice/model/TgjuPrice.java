package com.example.goldPrice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;

@Entity
@Table(name = "tgju")
public class TgjuPrice {
    @Id
    private long id = 1L;

    @Column
    private double price;

    @Column
    private String fetchedAt; ;

    @OneToOne
    @JoinColumn(name = "provider_id")
    private PriceProviders priceProvider;

    public TgjuPrice(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(String fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public PriceProviders getPriceProvider() {
        return priceProvider;
    }

    public void setPriceProvider(PriceProviders priceProvider) {
        this.priceProvider = priceProvider;
    }
}
