package com.bonge.airportfly.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.WindowManager.LayoutParams
import androidx.core.view.isVisible
import com.bonge.airportfly.R
import com.bonge.airportfly.databinding.DialogCommonBinding
import com.bonge.airportfly.utils.App
import com.bonge.airportfly.utils.Util.getWindexProportionWidth

class CommonDialog(private val context: Context) {

    companion object {
        private var isShowDialog = false
    }

    private var dialog: AlertDialog = AlertDialog.Builder(context).create()
    private val binding = DialogCommonBinding.inflate(LayoutInflater.from(context))
    private var isCancelable = true
    private var title = App.context.getString(R.string.info)
    private var message: String = ""
    private var confirmText: String? = App.context.getString(R.string.confirm)
    private var cancelText: String? = null
    private var confirmOnClickListener: OnClickListener? = null
    private var cancelOnClickListener: OnClickListener? = null

    init {
        dialog.setView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setCancelable(isCancelable: Boolean): CommonDialog {
        this.isCancelable = isCancelable
        return this
    }

    fun setTitle(text: String): CommonDialog {
        title = text
        return this
    }

    fun setMessage(text: String): CommonDialog {
        message = text
        return this
    }

    fun setPositiveButton(
        text: String? = confirmText,
        onClickListener: OnClickListener? = null
    ): CommonDialog {
        confirmText = text
        confirmOnClickListener = onClickListener
        return this
    }

    fun setNegativeButton(
        text: String? = App.context.getString(R.string.cancel),
        onClickListener: OnClickListener? = null
    ): CommonDialog {
        cancelText = text
        cancelOnClickListener = onClickListener
        return this
    }

    fun show() {
        if (!isShowDialog) {
            isShowDialog = true
            dialog.apply {
                setCancelable(isCancelable)

                binding.titleTextView.text = title
                binding.messageTextView.text = message

                binding.confirmButton.text = confirmText
                binding.confirmButton.setOnClickListener {
                    dialog.dismiss()
                    confirmOnClickListener?.onClick(it)
                }

                binding.cancelButton.isVisible = cancelText != null
                cancelText?.let {
                    binding.cancelButton.setOnClickListener {
                        dialog.dismiss()
                        cancelOnClickListener?.onClick(it)
                    }
                }

                dialog.setOnDismissListener {
                    isShowDialog = false
                }
            }
            dialog.show()
            dialog.window?.setLayout(
                getWindexProportionWidth(context),
                LayoutParams.WRAP_CONTENT
            )
        }

    }

}