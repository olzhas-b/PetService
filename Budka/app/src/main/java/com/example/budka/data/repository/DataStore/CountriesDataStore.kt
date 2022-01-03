/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.repository.Base.BaseCountries
import com.example.budka.data.repository.Base.BasePetSittersDataStore
import com.example.budka.domain.repository.CountriesRepository
import com.example.budka.domain.repository.PetSittersListRepository

class CountriesDataStore (apiService: ApiService) : CountriesRepository, BaseCountries(apiService) {
    override fun getAllCountries(): LiveData<List<CountryData>> {
        return fetchData { service.getCountries("https://countriesnow.space/api/v0.1/countries") }
    }
}