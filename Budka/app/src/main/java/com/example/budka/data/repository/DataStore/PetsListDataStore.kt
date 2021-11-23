package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.Pet
import com.example.budka.data.repository.Base.BasePetsDataStore
import com.example.budka.domain.repository.PetsListRepository

class PetsListDataStore(apiService: ApiService): PetsListRepository, BasePetsDataStore(apiService) {

    override fun getAllPets(): LiveData<List<Pet>> {
        return fetchData { service.getPets()}
    }

    override fun getUserPets(user_id: Int): LiveData<List<Pet>> {
        return fetchData { service.getUserPets(user_id) }
    }
}