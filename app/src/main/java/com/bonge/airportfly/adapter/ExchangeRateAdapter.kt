package com.bonge.airportfly.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonge.airportfly.databinding.ItemExchangeRateBinding
import com.bonge.airportfly.databinding.ItemFlightBinding
import com.bonge.airportfly.enum.Currency
import com.bonge.airportfly.utils.App.Companion.context
import com.bonge.airportfly.web.ExchangeRateData
import java.text.DecimalFormat

class ExchangeRateAdapter : RecyclerView.Adapter<ExchangeRateAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(currency: Currency)
    }

    var list: List<Currency> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemExchangeRateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(ItemExchangeRateBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val item = list[position]
            currencyTextView.text = item.name
            val decimalFormat=DecimalFormat("#,##0.00")
            currencyValueTextView.text = decimalFormat.format(item.value)
            root.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
        }
    }

    override fun getItemCount() = list.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

}