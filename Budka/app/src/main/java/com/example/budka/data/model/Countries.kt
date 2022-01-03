/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

data class Countries (
    var error: Boolean,
    var msg: String,
    var data : List<CountryData>,
    )

data class CountryData(
    var country: String,
    var cities: List<String>
)