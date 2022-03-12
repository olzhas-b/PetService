/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetType
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User

interface PetSittersListRepository {
    fun getPetSitters(serviceType: Int, country: String?, city: String?, petType: String?): LiveData<List<ServiceProvider>>
    fun putLike(serviceId: Int): LiveData<String>
    fun deleteLike(serviceId: Int): LiveData<String>
    fun getFavoriteServices(): LiveData<List<ServiceProvider>>

}