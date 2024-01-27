package com.bonge.airportfly.utils

import android.app.Dialog
import android.content.Context
import com.bonge.airportfly.R
import com.bonge.airportfly.view.dialog.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call

suspend fun <T> Call<T>.exec(context: Context, onSuccess: suspend (response: T) -> Unit = {}) {

    suspend fun showNetworkErrorDialog() {
        withMain {
            CommonDialog(context)
                .setCancelable(false)
                .setMessage(context.getString(R.string.please_check_network))
                .setPositiveButton(context.getString(R.string.retry_connect)) {
                    context.loading {
                        clone().exec(context, onSuccess)
                    }
                }
                .show()
        }
    }

    try {
        val request = request()
        LogUtil.d("[API Request] $request")
        val execute = execute()
        LogUtil.d("[API Response] ${request.url()} , ${execute.body()}")
        if (execute.isSuccessful) {
            val body = execute.body()
            if (body == null) {
                LogUtil.d("[API Response] body = null")
            } else {
                onSuccess.invoke(body)
            }
        } else {
            execute.errorBody()?.let {
                LogUtil.d("[API Response Error] ${it.string()}")
            }
            showNetworkErrorDialog()
        }
    } catch (ex: Exception) {
        LogUtil.e("[API Response Error] $ex", ex)
        showNetworkErrorDialog()
    }
}

fun launch(unit: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        unit.invoke()
    }
}

suspend fun withMain(unit: () -> Unit) {
    withContext(Dispatchers.Main) {
        unit.invoke()
    }
}

fun Context.loading(unit: suspend () -> Unit) {
    val dialog = Dialog(this).apply {
        setContentView(R.layout.layout_loading)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        show()
    }
    CoroutineScope(Dispatchers.IO).launch {
        unit.invoke()
        withMain {
            dialog.dismiss()
        }
    }
}