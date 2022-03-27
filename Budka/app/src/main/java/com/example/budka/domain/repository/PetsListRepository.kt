/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetCreate
import okhttp3.MultipartBody

interface PetsListRepository {
    fun getAllPets(): LiveData<NetworkResult<List<Pet>>>
    fun getUserPets(user_id:Int): LiveData<NetworkResult<List<Pet>>>
    fun createPet(
        image: MultipartBody.Part,
        body: PetCreate
    ):  LiveData<NetworkResult<Pet>>
    fun updatePet(
        image: MultipartBody.Part,
        body: PetCreate,
        petId: Int
    ):  LiveData<NetworkResult<Pet>>

    fun deletePet(petId: Int):  LiveData<NetworkResult<String>>
}