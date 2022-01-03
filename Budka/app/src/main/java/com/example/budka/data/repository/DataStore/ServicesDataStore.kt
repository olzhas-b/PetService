package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.Pet
import com.example.budka.data.model.Services
import com.example.budka.data.repository.Base.BasePetsDataStore
import com.example.budka.data.repository.Base.BaseServicesDataStore
import com.example.budka.domain.repository.PetsListRepository
import com.example.budka.domain.repository.ServicesRepository

class ServicesDataStore(apiService: ApiService): ServicesRepository, BaseServicesDataStore(apiService) {

    override fun getUserServices(user_id: Int): LiveData<List<Services>> {
        return fetchData { service.getUserServices(user_id) }
    }
}