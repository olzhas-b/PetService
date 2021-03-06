/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetCreate
import com.example.budka.data.repository.Base.BasePetsDataStore
import com.example.budka.domain.repository.PetsListRepository
import okhttp3.MultipartBody

class PetsListDataStore(apiService: ApiService): PetsListRepository, BasePetsDataStore(apiService) {

    override fun getAllPets(): LiveData<NetworkResult<List<Pet>>> {
        return fetchData { service.getPets()}
    }

    override fun getUserPets(user_id: Int): LiveData<NetworkResult<List<Pet>>> {
        return fetchData { service.getUserPets(user_id) }
    }

    override fun getPetDetail(petId: Int): LiveData<NetworkResult<Pet>> {
        return getPetResponse { service.getPetDetail(petId) }
    }

    override fun uploadAttachment(petId: Int, attachments: List<MultipartBody.Part>): LiveData<NetworkResult<String>> {
        return deleteResponse { service.uploadAttachment(petId, attachments) }
    }

    override fun createPet(image: MultipartBody.Part,
                        body: PetCreate
    ):  LiveData<NetworkResult<Pet>> {
        return getPetResponse { service.createPet(image, body) }
    }

    override fun updatePet(image: MultipartBody.Part, body: PetCreate, petId: Int):  LiveData<NetworkResult<Pet>> {
        return getPetResponse { service.updatePet(image, body, petId) }
    }

    override fun deletePet(petId: Int):  LiveData<NetworkResult<String>> {
        return deleteResponse {service.deletePet(petId)}
    }

    override fun deletePetDoc(petId: Int):  LiveData<NetworkResult<String>> {
        return deleteResponse {service.deletePetDoc(petId)}
    }
}