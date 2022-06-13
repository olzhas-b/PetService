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
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetCreate
import com.example.budka.domain.useCase.PetsListUseCase
import okhttp3.MultipartBody

class PetsListViewModel(private val petsListUseCase: PetsListUseCase): BaseViewModel() {
    private var petsList = MutableLiveData<NetworkResult<List<Pet>>>()
    private var userPetsList = MutableLiveData<NetworkResult<List<Pet>>>()

    fun fetchPetsList(){
        petsList = petsListUseCase.getPetsList() as MutableLiveData<NetworkResult<List<Pet>>>
    }

    fun getPetsList(): LiveData<NetworkResult<List<Pet>>>{
        return petsList
    }

    fun fetchUserPetsList(user_id: Int){
        userPetsList = petsListUseCase.getUserPets(user_id) as MutableLiveData<NetworkResult<List<Pet>>>
    }

    fun getPetDetail(petId: Int): LiveData<NetworkResult<Pet>>{
        return petsListUseCase.getPetDetail(petId)
    }

    fun uploadAttachment(petId: Int, attachments: List<MultipartBody.Part>): LiveData<NetworkResult<String>>{
        return petsListUseCase.uploadAttachment(petId, attachments)
    }

//    fun fetchUserPetsList(user_id: Int): LiveData<NetworkResult<List<Pet>>>{
//        return userPetsList = petsListUseCase.getUserPets(user_id) as MutableLiveData<NetworkResult<List<Pet>>>
//    }

    fun getUserPetsList(): LiveData<NetworkResult<List<Pet>>>{
        return userPetsList
    }

    fun createPet(image: MultipartBody.Part,
                      body: PetCreate
    ): LiveData<NetworkResult<Pet>>{
        return petsListUseCase.createPet(image, body)
    }

    fun updatePet(image: MultipartBody.Part,
                  body: PetCreate,
                  petId: Int
    ): LiveData<NetworkResult<Pet>>{
        return petsListUseCase.updatePet(image, body, petId)
    }

    fun deletePet(petId: Int): LiveData<NetworkResult<String>> {
       return petsListUseCase.deletePet(petId)
    }

    fun deletePetDoc(petId: Int): LiveData<NetworkResult<String>> {
        return petsListUseCase.deletePetDoc(petId)
    }

}