package com.example.goldPrice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TgjuResponse(@JsonProperty("current") Current current) {
    public record Current (@JsonProperty("geram18") Price geram18){}
    public record Price (@JsonProperty("p") String price) {}
}
