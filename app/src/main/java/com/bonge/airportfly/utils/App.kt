package com.bonge.airportfly.utils

import android.app.Application
import android.content.Context
import com.bonge.airportfly.web.AirportFlyApi
import com.bonge.airportfly.web.ExchangeRateApi
import com.bonge.airportfly.web.Server
import kotlin.properties.Delegates

class App : Application() {
    companion object {
        var airportFlyApi: AirportFlyApi by Delegates.notNull()
        var exchangeRateApi: ExchangeRateApi by Delegates.notNull()
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        airportFlyApi = Server.airportFlyApi
        exchangeRateApi = Server.exchangeRateApi
        context = this
    }

}