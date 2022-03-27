/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

data class CurrencyModel(
    var currencyCode: String?=null
)

enum class CurrencyType(val value: String) {
    KZT("KZT"),
    RUB("RUB"),
    USD("USD");
    companion object{
        fun from(s: String): PricePerTime? = PricePerTime.values().find { it.value == s }
    }

}

enum class PricePerTime(val value: String) {
    Nothing(" "),
    HOUR("час"),
    DAY("в день"),
    WALK("прогулка");
    companion object{
        fun from(s: String): PricePerTime? = values().find { it.value == s }
    }



}


