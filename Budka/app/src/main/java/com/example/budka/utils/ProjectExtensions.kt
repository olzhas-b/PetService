package com.example.budka.utils

import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import com.example.budka.R
import com.example.budka.data.model.CurrencyModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*



fun TextView.setUpPriceMask(
    text: String?,
    currencyModel: CurrencyModel?= null,
    pricePerTime: String?
){
    setMaxLength(11)
    try{
        val amount = text?.toInt()
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.decimalSeparator = '.'
        formatSymbols.groupingSeparator = ' '
        val localeKz = Locale("ru", "kz")
        val currency = Currency.getInstance(currencyModel?.currencyCode?:"KZT")

        if (pricePerTime != null) {
            val finalText = "$amount ${currency.getSymbol(localeKz)}/$pricePerTime"
            val spannable =  SpannableString(finalText)
            spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.pricePerAmount)), finalText.length-(pricePerTime.length+1), finalText.length,0 )
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
        else{
            val finalText = "$amount ${currency.getSymbol(localeKz)}"
            setText(finalText)
        }}
    catch (e: Exception){
        e.printStackTrace()
    }

}

fun TextView.setMaxLength(maxLength: Int): TextView{
    filters = filters.toMutableList().apply {
        removeAll{ it is InputFilter.LengthFilter}
        add(InputFilter.LengthFilter(maxLength))
    }.toTypedArray()
    return  this
}