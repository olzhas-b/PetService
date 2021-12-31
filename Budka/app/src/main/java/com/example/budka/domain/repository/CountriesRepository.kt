package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.ServiceProvider

interface CountriesRepository {
    fun getAllCountries(): LiveData<List<CountryData>>
}