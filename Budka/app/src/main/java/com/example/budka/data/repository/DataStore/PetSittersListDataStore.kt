/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.data.repository.Base.BasePetSittersDataStore
import com.example.budka.domain.repository.PetSittersListRepository

class PetSittersListDataStore(apiService: ApiService) : PetSittersListRepository, BasePetSittersDataStore(apiService) {
    override fun getPetSitters(serviceType: String): LiveData<List<ServiceProvider>> {
        return fetchData { service.getPetSitters(serviceType) }
    }
}