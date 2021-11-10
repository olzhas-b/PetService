package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet

interface PetsListRepository {
    fun petsData(): LiveData<List<Pet>>
}