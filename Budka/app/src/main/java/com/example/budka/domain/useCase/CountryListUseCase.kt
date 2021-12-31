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