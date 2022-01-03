/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.Services
import com.example.budka.domain.repository.CountriesRepository
import com.example.budka.domain.repository.ServicesRepository

class CountryListUseCase (private val countriesRepository: CountriesRepository){
    fun getCountries(): LiveData<List<CountryData>> {
        return countriesRepository.getAllCountries()
    }
}