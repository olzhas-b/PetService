/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.data.repository.DataStore

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.ServiceProviderResponse
import com.example.budka.data.repository.Base.BasePetSittersDataStore
import com.example.budka.domain.repository.PetSittersListRepository


class PetSittersListDataStore(apiService: ApiService, context: Context) : PetSittersListRepository, BasePetSittersDataStore(
    apiService, context
) {
    override fun getPetSitters(serviceType: Int, country: String?, city: String?, petType: String?):  LiveData<NetworkResult<ServiceProviderResponse>> {
        val data: MutableMap<String, String?> = HashMap()
        data["serviceType"] = serviceType.toString()
        data["country"] = country?: run { "" }
        data["city"] = city?: run { "" }
        data["petType"] = petType?: run { "" }
        return fetchData { service.getPetSitters(
            data) }
    }

    override fun putLike(serviceId: Int):  LiveData<NetworkResult<String>> {
        return favResponse {service.putLike(serviceId)  }
    }

    override fun deleteLike(serviceId: Int):  LiveData<NetworkResult<String>> {
        return favResponse {service.deleteLike(serviceId)  }
    }

    override fun getFavoriteServices():  LiveData<NetworkResult<List<ServiceProvider>>> {
        return fetchDataFav { service.getFavoriteServices() }
    }
}