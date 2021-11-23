package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User

interface PetSittersListRepository {
    fun getPetSitters(serviceType: String): LiveData<List<ServiceProvider>>
}