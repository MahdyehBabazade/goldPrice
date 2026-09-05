package com.example.goldPrice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "price_providers")
public class PriceProviders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    public PriceProviders(){ }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {return name;}
    public void setName(String names) {this.name = names;}

}
