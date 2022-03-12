/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetCreate
import com.example.budka.data.model.PetResponse
import com.example.budka.domain.repository.PetsListRepository
import okhttp3.MultipartBody

class PetsListUseCase(val petsListRepository: PetsListRepository){
    fun getPetsList(): LiveData<List<Pet>>{
        return petsListRepository.getAllPets()
    }

    fun getUserPets(user_id: Int): LiveData<List<Pet>>{
        return petsListRepository.getUserPets(user_id)
    }

    fun createPet(
        image: MultipartBody.Part,
        body: PetCreate
    ) : LiveData<Pet>{
        return petsListRepository.createPet(image, body)
    }

    fun updatePet(
        image: MultipartBody.Part,
        body: PetCreate,
        petId: Int
    ) : LiveData<Pet>{
        return petsListRepository.updatePet(image, body, petId)
    }

    fun deletePet(petId: Int){
        petsListRepository.deletePet(petId)
    }
}