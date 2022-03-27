/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.utils

import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.TextView
import com.example.budka.R
import com.example.budka.data.model.CurrencyModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*



fun TextView.setUpPriceMask(
    text: String?,
    currencyCode: String?= null,
    pricePerTime: String?
){
    try{
        val amount = text?.toInt()
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.decimalSeparator = '.'
        formatSymbols.groupingSeparator = ' '
        val localeKz = Locale("ru", "kz")
        if(currencyCode?.length==3) {
            val currency = Currency.getInstance(currencyCode ?: "KZT")

            if (pricePerTime != null) {
                val finalText = "$amount ${currency.getSymbol(localeKz)}/$pricePerTime"
                val spannable = SpannableString(finalText)
                spannable.setSpan(
                    ForegroundColorSpan(resources.getColor(R.color.pricePerAmount)),
                    finalText.length - (pricePerTime.length + 1),
                    finalText.length,
                    0
                )
                spannable.setSpan( RelativeSizeSpan(0.8f),
                    finalText.length - (pricePerTime.length + 1),
                    finalText.length,
                    0);
                setText(spannable, TextView.BufferType.SPANNABLE)
            } else {
                val finalText = "$amount ${currency.getSymbol(localeKz)}"
                setText(finalText)
            }
        }
        else{
            setText("$amount")
        }
    }
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