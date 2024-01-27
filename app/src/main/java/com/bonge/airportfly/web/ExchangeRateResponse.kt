package com.bonge.airportfly.web

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("data")
    val data: ExchangeRateData
)

data class ExchangeRateData(
    val JPY: Double,
    val USD: Double,
    val CNY: Double,
    val EUR: Double,
    val AUD: Double,
    val KRW: Double
)
