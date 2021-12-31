package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.Services

interface ServicesRepository {
    fun getUserServices(user_id:Int): LiveData<List<Services>>
}