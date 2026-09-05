package com.example.goldPrice.client;

import com.example.goldPrice.dto.TalaseaResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PriceApiClient {
    @GET("api/market/getGoldPrice")
    Call<TalaseaResponse> getTalaseaPrice();
}
