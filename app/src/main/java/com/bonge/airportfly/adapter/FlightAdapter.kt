package com.bonge.airportfly.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bonge.airportfly.R
import com.bonge.airportfly.databinding.ItemFlightBinding
import com.bonge.airportfly.enum.Remark
import com.bonge.airportfly.web.FlightResponse

class FlightAdapter : RecyclerView.Adapter<FlightAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFlightBinding) : RecyclerView.ViewHolder(binding.root)

    var list: List<FlightResponse> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(ItemFlightBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val item = list[position]
            estimatedTimeTextView.text = item.estimatedTime
            actualTimeTextView.text = item.actualTime
            flightNumberTextView.text =
                String.format(context.getString(R.string.flight_number), item.flightNumber)
            gateWayTextView.text =
                String.format(context.getString(R.string.gate_way), item.terminal, item.gate ?: "")
            departureAirportIdTextView.text = item.departureAirportID
            departureAirportTextView.text = item.departureAirport
            arrivalAirportIdTextView.text = item.arrivalAirportID
            arrivalAirportTextView.text = item.arrivalAirport

            remarkTextView.text = item.remark
            val remark = Remark.values().find {
                item.remark.contains(it.name)
            }
            when (remark) {
                Remark.出發 -> {
                    remarkTextView.setTextColor(context.getColor(R.color.green))
                }
                Remark.已到 -> {
                    remarkTextView.setTextColor(context.getColor(R.color.blue))
                }
                Remark.取消 -> {
                    remarkTextView.setTextColor(context.getColor(R.color.red))
                }
                Remark.時間更改 -> {
                    remarkTextView.setTextColor(context.getColor(R.color.orange))
                }
                else -> {
                    remarkTextView.setTextColor(context.getColor(R.color.red))
                }
            }
        }
    }

    override fun getItemCount() = list.size

}