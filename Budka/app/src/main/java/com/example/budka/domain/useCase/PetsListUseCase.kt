package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.domain.repository.PetsListRepository

class PetsListUseCase(val petsListRepository: PetsListRepository){
    fun getPetsList(): LiveData<List<Pet>>{
        return petsListRepository.getAllPets()
    }

    fun getUserPets(user_id: Int): LiveData<List<Pet>>{
        return petsListRepository.getUserPets(user_id)
    }
}