package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet

interface PetsListRepository {
    fun getAllPets(): LiveData<List<Pet>>
    fun getUserPets(user_id:Int): LiveData<List<Pet>>
}