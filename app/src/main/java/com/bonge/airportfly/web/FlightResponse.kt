package com.bonge.airportfly.web

data class FlightResponse(
    val flightNumber: String,
    val departureAirportID: String,
    val departureAirport: String,
    val arrivalAirportID: String,
    val arrivalAirport: String,
    val estimatedTime: String,
    val actualTime: String,
    val remark: String,
    val terminal: String,
    val gate: String?
)
