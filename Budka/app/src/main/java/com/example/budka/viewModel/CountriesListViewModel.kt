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