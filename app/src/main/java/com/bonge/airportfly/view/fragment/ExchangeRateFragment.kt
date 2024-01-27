package com.bonge.airportfly.view.fragment

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bonge.airportfly.R
import com.bonge.airportfly.adapter.ExchangeRateAdapter
import com.bonge.airportfly.databinding.FragmentExchangeRateBinding
import com.bonge.airportfly.enum.Currency
import com.bonge.airportfly.utils.App
import com.bonge.airportfly.utils.exec
import com.bonge.airportfly.utils.loading
import com.bonge.airportfly.view.dialog.NumberKeyboardBottomSheetDialog
import com.bonge.airportfly.viewModel.ExchangeRateViewModel

class ExchangeRateFragment : BaseFragment<FragmentExchangeRateBinding>(
    FragmentExchangeRateBinding::class.java,
    R.layout.fragment_exchange_rate
) {

    private val viewModel: ExchangeRateViewModel by activityViewModel()
    private val adapter = ExchangeRateAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.setOnItemClickListener(object : ExchangeRateAdapter.OnItemClickListener {
            override fun onItemClick(currency: Currency) {
                context?.let {
                    NumberKeyboardBottomSheetDialog(it, currency)
                        .setOnConfirmListener(object :
                            NumberKeyboardBottomSheetDialog.OnConfirmListener {
                            override fun onCallback(currency: Currency) {
                                getExchangeRate(currency)
                            }

                        }).show()
                }

            }

        })

        getExchangeRate(Currency.USD)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.exchangeRateRecyclerView?.adapter = adapter
    }

    override fun invalidate() {
        withState(viewModel) {
            adapter.list = it.currencyList
        }
    }

    private fun getExchangeRate(currency: Currency) {
        context?.let { context ->
            withState(viewModel) { state ->
                context.loading {
                    App.exchangeRateApi.getExchangeRate(currency.name,state.currencies)
                        .exec(context) {
                            viewModel.setExchangeRateData(
                                currency.value,
                                it.data
                            )
                        }
                }
            }
        }
    }
}