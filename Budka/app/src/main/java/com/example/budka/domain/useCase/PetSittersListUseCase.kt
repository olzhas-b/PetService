package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.User
import com.example.budka.domain.repository.PetSittersListRepository
import com.example.budka.domain.repository.PetsListRepository

class PetSittersListUseCase (val petSittersListRepository: PetSittersListRepository){
    fun getPetSittersList(): LiveData<List<User>> {
        return petSittersListRepository.nearestPetSitters()
    }
}