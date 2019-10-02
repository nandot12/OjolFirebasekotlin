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


/// date utils bang nando

object DateUtils {
    const val DATE_FORMAT_YYYY = "yyyy-MM-dd"
    const val DATE_FORMAT_FULL = "EEEE dd MMM yyyy"
    const val DATE_FORMAT_HISTORY = "dd MMMM yyyy"

    fun formatStringToDate(date: String, format: String) : Date{
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.parse(date)
    }

    fun formatDateLong(date: Long, destinationFormat: String) : String{
        val sdfDest = SimpleDateFormat(destinationFormat, Locale.getDefault())
        return sdfDest.format(Date(date))
    }

    fun formatDateLongLocale(date: Long, destinationFormat: String) : String{
        val sdfDest = SimpleDateFormat(destinationFormat, Locale("in","ID"))
        return sdfDest.format(Date(date))
    }
    fun formatDateLocale(date: Date, destinationFormat: String) : String{
        val sdfDest = SimpleDateFormat(destinationFormat, Locale("in","ID"))
        return sdfDest.format(date)
    }

}


