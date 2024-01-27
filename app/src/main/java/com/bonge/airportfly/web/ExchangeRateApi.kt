package com.bonge.airportfly.web

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest?apikey=fca_live_OBn0NdVVgG6d7kBvdVNor06cGqEyXTGkpW9pj7Xf")
    fun getExchangeRate(
        @Query("base_currency") baseCurrency: String?,
        @Query("currencies") currencies: String?
    ): retrofit2.Call<ExchangeRateResponse>
}