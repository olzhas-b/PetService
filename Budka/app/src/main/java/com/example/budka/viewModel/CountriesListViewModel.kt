/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.ServiceProvider
import com.example.budka.domain.useCase.CountryListUseCase
import com.example.budka.domain.useCase.PetSittersListUseCase

class CountriesListViewModel (private val countryListUseCase: CountryListUseCase): BaseViewModel() {
    private var countryList = MutableLiveData<List<CountryData>>()


    fun fetchCountryList() : LiveData<List<CountryData>>{
        return countryListUseCase.getCountries() as MutableLiveData<List<CountryData>>
    }


}