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

    private val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
    private val binding = DialogNumberKeyboardBinding.inflate(LayoutInflater.from(context))
    private var onConfirmListener: OnConfirmListener? = null
    private val onClickListener = View.OnClickListener {
        val value = binding.currencyValueTextView.text.toString()
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
            R.id.dismiss_button -> {
                dialog.dismiss()
            }
            R.id.cancel_button -> {
                binding.currencyValueTextView.text = ""
            }
            R.id.backspace_button -> {
                if (value.isNotEmpty()) {
                    binding.currencyValueTextView.text = value.substring(0, value.length - 1)
                }
            }
            R.id.confirm_button -> {
                if (checkInput()) {
                    currency.value = value.toDouble()
                    onConfirmListener?.onCallback(currency)
                    dialog.dismiss()
                }
            }
        }
    }

    init {
        binding.apply {
            currencyTextView.text = currency.name
            number0Button.setOnClickListener(onClickListener)
            number1Button.setOnClickListener(onClickListener)
            number2Button.setOnClickListener(onClickListener)
            number3Button.setOnClickListener(onClickListener)
            number4Button.setOnClickListener(onClickListener)
            number5Button.setOnClickListener(onClickListener)
            number6Button.setOnClickListener(onClickListener)
            number7Button.setOnClickListener(onClickListener)
            number8Button.setOnClickListener(onClickListener)
            number9Button.setOnClickListener(onClickListener)
            numberPointButton.setOnClickListener(onClickListener)
            confirmButton.setOnClickListener(onClickListener)
            dismissButton.setOnClickListener(onClickListener)
            cancelButton.setOnClickListener(onClickListener)
            backspaceButton.setOnClickListener(onClickListener)
            dialog.setContentView(root)
        }
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