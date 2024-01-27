package com.bonge.airportfly.utils

import android.util.Log
import com.bonge.airportfly.BuildConfig


object LogUtil {

    private const val TAG = BuildConfig.APPLICATION_ID

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }

}