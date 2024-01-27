package com.bonge.airportfly.viewModel

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.bonge.airportfly.web.FlightResponse

data class FlightState(
    var departingFlightList: List<FlightResponse> = listOf(),
    var arrivalFlightList:List<FlightResponse> = listOf()
) : MvRxState

class FlightViewModel(state: FlightState) : BaseMvRxViewModel<FlightState>(state, false) {
    fun setDepartingFlightList(departingFlightList: List<FlightResponse>) {
        setState {
            this.departingFlightList= listOf()
            copy(
                departingFlightList = departingFlightList
            )
        }
    }

    fun setArrivalFlightList(arrivalFlightList: List<FlightResponse>) {
        setState {
            this.arrivalFlightList= listOf()
            copy(
                arrivalFlightList = arrivalFlightList
            )
        }
    }
}