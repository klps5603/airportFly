package com.bonge.airportfly.view.fragment

import android.os.Bundle
import android.view.View
import com.bonge.airportfly.R
import com.bonge.airportfly.adapter.ViewPagerAdapter
import com.bonge.airportfly.databinding.FragmentFlightBinding
import com.google.android.material.tabs.TabLayoutMediator

class FlightFragment : BaseFragment<FragmentFlightBinding>(
    FragmentFlightBinding::class.java,
    R.layout.fragment_flight
) {

    private val adapter by lazy {
        ViewPagerAdapter(this).apply {
            list = listOf(DepartingFlightFragment(), ArrivalFlightFragment())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            flightViewPager.isSaveEnabled = false
            flightViewPager.adapter = adapter
            TabLayoutMediator(
                flightTabLayout,
                flightViewPager
            ) { tab, position ->
                val array = resources.getStringArray(R.array.fly_type)
                tab.text = array[position]
                when (position) {
                    0 -> {
                        tab.setIcon(R.drawable.ic_departing_flight)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_arrival_flight)
                    }
                }
            }.attach()
        }
    }

    override fun invalidate() {

    }
}