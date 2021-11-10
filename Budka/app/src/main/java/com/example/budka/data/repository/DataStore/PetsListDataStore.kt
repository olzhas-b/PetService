package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.repository.Base.BasePetsDataStore
import com.example.budka.domain.repository.PetsListRepository

class PetsListDataStore: PetsListRepository, BasePetsDataStore() {
    override fun petsData(): LiveData<List<Pet>>{
        return fetchData()
    }

    override fun loadData(): LiveData<List<Pet>> {
        TODO("Not yet implemented")
    }
}