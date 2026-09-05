package com.example.goldPrice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;

@Entity
@Table(name = "talasea")
public class TalaseaPrice {

    @Id
    private long id = 1L;

    @Column
    private double price;

    @OneToOne
    @JoinColumn(name = "provider_id")
    private PriceProviders priceProvider;

    public TalaseaPrice(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonValue
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPriceProvider(PriceProviders priceProvider) {
        this.priceProvider = priceProvider;
    }
}
