/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.ServiceProvider

interface CountriesRepository {
    fun getAllCountries(): LiveData<List<CountryData>>
}