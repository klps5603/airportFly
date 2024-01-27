package com.bonge.airportfly.web

import retrofit2.http.GET
import retrofit2.http.Path

interface AirportFlyApi {

    @GET("AirPortFlyAPI/{flyType}/TPE")
    fun getAirPortFly(
        @Path("flyType") flyType: String
    ): retrofit2.Call<List<FlightResponse>>

}