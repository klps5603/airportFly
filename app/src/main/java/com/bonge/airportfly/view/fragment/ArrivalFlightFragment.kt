package com.bonge.airportfly.view.fragment

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bonge.airportfly.R
import com.bonge.airportfly.adapter.FlightAdapter
import com.bonge.airportfly.databinding.FragmentArrivalFlightBinding
import com.bonge.airportfly.utils.App
import com.bonge.airportfly.utils.exec
import com.bonge.airportfly.utils.launch
import com.bonge.airportfly.utils.loading
import com.bonge.airportfly.viewModel.FlightViewModel
import java.util.*

class ArrivalFlightFragment : BaseFragment<FragmentArrivalFlightBinding>(
    FragmentArrivalFlightBinding::class.java,
    R.layout.fragment_arrival_flight
) {
    private val viewModel: FlightViewModel by activityViewModel()
    private val adapter = FlightAdapter()
    private val airPortFlyTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.loading {
            getAirPortFly()
        }

        airPortFlyTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                launch {
                    getAirPortFly()
                }
            }

        }, 10000, 10000)

    }

    private suspend fun getAirPortFly() {
        context?.let { context ->
            App.airportFlyApi.getAirPortFly("A").exec(context) {
                viewModel.setArrivalFlightList(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.arrivalFlightRecyclerView?.adapter = adapter
    }

    override fun invalidate() {
        withState(viewModel) {
            adapter.list = it.arrivalFlightList
        }
    }
}