package com.bonge.airportfly.web

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Server {
    val airportFlyApi: AirportFlyApi
    val exchangeRateApi: ExchangeRateApi

    init {
        val client = OkHttpClient.Builder().build()
        val gson = GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create()
        )
        val airportFlyRetrofit = Retrofit.Builder()
            .baseUrl("https://e-traffic.taichung.gov.tw/DataAPI/api/")
            .addConverterFactory(gson)
            .client(client)
            .build()
        airportFlyApi = airportFlyRetrofit.create(AirportFlyApi::class.java)

        val exchangeRateRetrofit = Retrofit.Builder()
            .baseUrl("https://api.freecurrencyapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        exchangeRateApi = exchangeRateRetrofit.create(ExchangeRateApi::class.java)
    }

}