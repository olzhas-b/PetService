package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.model.User
import com.example.budka.data.repository.Base.BasePetSittersDataStore
import com.example.budka.domain.repository.PetSittersListRepository

class PetSittersListDataStore : PetSittersListRepository, BasePetSittersDataStore() {
    override fun nearestPetSitters(): LiveData<List<User>> {
        return fetchNearestPetSitterData()
    }
}