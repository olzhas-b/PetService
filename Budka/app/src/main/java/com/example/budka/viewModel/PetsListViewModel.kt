/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetCreate
import com.example.budka.domain.useCase.PetsListUseCase
import okhttp3.MultipartBody

class PetsListViewModel(private val petsListUseCase: PetsListUseCase): BaseViewModel() {
    private var petsList = MutableLiveData<List<Pet>>()
    private var userPetsList = MutableLiveData<List<Pet>>()
    init {
        fetchPetsList()
    }
    fun fetchPetsList(){
        petsList = petsListUseCase.getPetsList() as MutableLiveData<List<Pet>>
    }

    fun getPetsList(): LiveData<List<Pet>>{
        return petsList
    }

    fun fetchUserPetsList(user_id: Int){
        userPetsList = petsListUseCase.getUserPets(user_id) as MutableLiveData<List<Pet>>
    }

    fun getUserPetsList(): LiveData<List<Pet>>{
        return userPetsList
    }

    fun createPet(image: MultipartBody.Part,
                      body: PetCreate
    ): LiveData<Pet>{
        return petsListUseCase.createPet(image, body)
    }

    fun updatePet(image: MultipartBody.Part,
                  body: PetCreate,
                  petId: Int
    ): LiveData<Pet>{
        return petsListUseCase.updatePet(image, body, petId)
    }

    fun deletePet(petId: Int){
        petsListUseCase.deletePet(petId)
    }

}