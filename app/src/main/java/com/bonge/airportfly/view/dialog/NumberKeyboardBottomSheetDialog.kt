package com.bonge.airportfly.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.bonge.airportfly.R
import com.bonge.airportfly.databinding.DialogNumberKeyboardBinding
import com.bonge.airportfly.enum.Currency
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.regex.Pattern

class NumberKeyboardBottomSheetDialog(
    private val context: Context,
    private val currency: Currency
) {

    companion object {
        private var isShowDialog = false
    }

    interface OnConfirmListener {
        fun onCallback(currency: Currency)
    }

    private val dialog = BottomSheetDialog(context)
    private val binding = DialogNumberKeyboardBinding.inflate(LayoutInflater.from(context))
    private var onConfirmListener: OnConfirmListener? = null
    private val onNumberClickListener = View.OnClickListener {
        val value = binding.currencyValueTextView.text
        when (it.id) {
            R.id.number_0_button -> {
                binding.currencyValueTextView.text = "${value}0"
            }
            R.id.number_1_button -> {
                binding.currencyValueTextView.text = "${value}1"
            }
            R.id.number_2_button -> {
                binding.currencyValueTextView.text = "${value}2"
            }
            R.id.number_3_button -> {
                binding.currencyValueTextView.text = "${value}3"
            }
            R.id.number_4_button -> {
                binding.currencyValueTextView.text = "${value}4"
            }
            R.id.number_5_button -> {
                binding.currencyValueTextView.text = "${value}5"
            }
            R.id.number_6_button -> {
                binding.currencyValueTextView.text = "${value}6"
            }
            R.id.number_7_button -> {
                binding.currencyValueTextView.text = "${value}7"
            }
            R.id.number_8_button -> {
                binding.currencyValueTextView.text = "${value}8"
            }
            R.id.number_9_button -> {
                binding.currencyValueTextView.text = "${value}9"
            }
            R.id.number_point_button -> {
                binding.currencyValueTextView.text = "${value}."
            }
        }
    }

    init {
        binding.apply {
            currencyTextView.text = currency.name
            number0Button.setOnClickListener(onNumberClickListener)
            number1Button.setOnClickListener(onNumberClickListener)
            number2Button.setOnClickListener(onNumberClickListener)
            number3Button.setOnClickListener(onNumberClickListener)
            number4Button.setOnClickListener(onNumberClickListener)
            number5Button.setOnClickListener(onNumberClickListener)
            number6Button.setOnClickListener(onNumberClickListener)
            number7Button.setOnClickListener(onNumberClickListener)
            number8Button.setOnClickListener(onNumberClickListener)
            number9Button.setOnClickListener(onNumberClickListener)
            numberPointButton.setOnClickListener(onNumberClickListener)
            confirmButton.setOnClickListener {
                if (checkInput()) {
                    currency.value=binding.currencyValueTextView.text.toString().toDouble()
                    onConfirmListener?.onCallback(currency)
                    dialog.dismiss()
                }
            }
            dialog.setContentView(root)
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun checkInput(): Boolean {
        val value = binding.currencyValueTextView.text.toString()
        if (value.isEmpty() || !Pattern.compile("^[0-9]+(.[0-9]+)?$").matcher(value).matches()) {
            Toast.makeText(
                context,
                context.getString(R.string.number_format_error),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    fun show() {
        if (!isShowDialog) {
            isShowDialog = true

            dialog.setOnDismissListener {
                isShowDialog = false
            }
            dialog.show()
        }
    }

    fun setOnConfirmListener(onConfirmListener: OnConfirmListener): NumberKeyboardBottomSheetDialog {
        this.onConfirmListener = onConfirmListener
        return this
    }
}