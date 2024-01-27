package com.bonge.airportfly.utils

import android.content.Context

object Util {
    /**
     * 取得佔螢幕比例的寬度
     */
    fun getWindexProportionWidth(context: Context, proportion: Double = 0.7): Int {
        return (context.resources.displayMetrics.widthPixels * proportion).toInt()
    }

    /**
     * 取得佔螢幕比例的寬度
     */
    fun getWindexProportionHeight(context: Context, proportion: Double = 0.5): Int {
        return (context.resources.displayMetrics.heightPixels * proportion).toInt()
    }

}