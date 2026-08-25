package com.example.goldPrice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prices")
public class Prices {

    @Id
    private long id = 1L;

    @Column
    private double tgju;

    @Column
    private double talasea;

    public Prices(){ }

    public Prices(double tgju, double talasea){
        this.tgju = tgju;
        this.talasea =talasea;
    }

    public void setId(long id) { this.id = id; }

    public double getTgju() {
        return tgju;
    }

    public void setTgju(double tgju) {
        this.tgju = tgju;
    }

    public double getTalasea() {
        return talasea;
    }

    public void setTalasea(double talasea) {
        this.talasea = talasea;
    }
}
