/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetCreate
import okhttp3.MultipartBody

interface PetsListRepository {
    fun getAllPets(): LiveData<List<Pet>>
    fun getUserPets(user_id:Int): LiveData<List<Pet>>
    fun createPet(
        image: MultipartBody.Part,
        body: PetCreate
    ): LiveData<Pet>
    fun updatePet(
        image: MultipartBody.Part,
        body: PetCreate,
        petId: Int
    ): LiveData<Pet>

    fun deletePet(petId: Int): LiveData<String>
}