/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.*
import com.example.budka.domain.repository.PetsListRepository
import okhttp3.MultipartBody

class PetsListUseCase(val petsListRepository: PetsListRepository){
    fun getPetsList(): LiveData<NetworkResult<List<Pet>>> {
        return petsListRepository.getAllPets()
    }

    fun getUserPets(user_id: Int): LiveData<NetworkResult<List<Pet>>>
    {
        return petsListRepository.getUserPets(user_id)
    }

    fun createPet(
        image: MultipartBody.Part,
        body: PetCreate
    ) : LiveData<NetworkResult<Pet>>{
        return petsListRepository.createPet(image, body)
    }

    fun updatePet(
        image: MultipartBody.Part,
        body: PetCreate,
        petId: Int
    ) : LiveData<NetworkResult<Pet>>{
        return petsListRepository.updatePet(image, body, petId)
    }

    fun deletePet(petId: Int): LiveData<NetworkResult<String>> {
        return petsListRepository.deletePet(petId)
    }
}