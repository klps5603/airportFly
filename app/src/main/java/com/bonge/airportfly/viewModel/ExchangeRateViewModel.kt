package com.bonge.airportfly.viewModel

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.bonge.airportfly.enum.Currency
import com.bonge.airportfly.web.ExchangeRateData

data class ExchangeRateState(
    val baseCurrency: Currency = Currency.USD,
    val currencies: String = "${Currency.JPY.name},${Currency.USD.name},${Currency.CNY.name},${Currency.EUR.name},${Currency.AUD.name},${Currency.KRW.name}",
    var currencyList: List<Currency> = listOf(),
    val exchangeRateData: ExchangeRateData? = null
) : MvRxState

class ExchangeRateViewModel(state: ExchangeRateState) :
    BaseMvRxViewModel<ExchangeRateState>(state, false) {

    fun setExchangeRateData(
        currencyValue: Double,
        exchangeRateData: ExchangeRateData
    ) {
        val currencyList = mutableListOf<Currency>()
        currencyList.add(Currency.JPY.calculateCurrencyValue(currencyValue, exchangeRateData))
        currencyList.add(Currency.USD.calculateCurrencyValue(currencyValue, exchangeRateData))
        currencyList.add(Currency.CNY.calculateCurrencyValue(currencyValue, exchangeRateData))
        currencyList.add(Currency.EUR.calculateCurrencyValue(currencyValue, exchangeRateData))
        currencyList.add(Currency.AUD.calculateCurrencyValue(currencyValue, exchangeRateData))
        currencyList.add(Currency.KRW.calculateCurrencyValue(currencyValue, exchangeRateData))
        setState {
            this.currencyList= listOf()
            copy(currencyList = currencyList)
        }
    }

    private fun Currency.calculateCurrencyValue(
        currencyValue: Double,
        exchangeRateData: ExchangeRateData
    ): Currency {
        return this.apply {
            when (this) {
                Currency.JPY -> {
                    value = exchangeRateData.JPY * currencyValue
                }
                Currency.USD -> {
                    value = exchangeRateData.USD * currencyValue
                }
                Currency.CNY -> {
                    value = exchangeRateData.CNY * currencyValue
                }
                Currency.EUR -> {
                    value = exchangeRateData.EUR * currencyValue
                }
                Currency.AUD -> {
                    value = exchangeRateData.AUD * currencyValue
                }
                Currency.KRW -> {
                    value = exchangeRateData.KRW * currencyValue
                }
            }
        }
    }

}