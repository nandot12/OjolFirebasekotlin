package com.udacoding.ojolfirebasekotlin.utils

import android.util.Log.d
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object ChangeFormat {

    fun toRupiahFormat2(nominal: String): String {

        val df = DecimalFormat.getCurrencyInstance() as DecimalFormat

        val dfs = DecimalFormatSymbols()
        dfs.setCurrencySymbol("")
        dfs.setMonetaryDecimalSeparator(',')
        dfs.setGroupingSeparator('.')
        df.setDecimalFormatSymbols(dfs)

        df.setMaximumFractionDigits(0)
        val rupiah = df.format(d(nominal))

        return rupiah


    }

    fun d(transPokok: String): Double? {
        var x: Double? = 0.0
        try {
            x = java.lang.Double.parseDouble(transPokok)
        } catch (e: Exception) {
            // TODO: handle exception
        }

        return x
    }

}


