/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.data.repository.DataStore

import android.R.string
import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.repository.Base.BasePetSittersDataStore
import com.example.budka.domain.repository.PetSittersListRepository
import java.net.URLEncoder


class PetSittersListDataStore(apiService: ApiService) : PetSittersListRepository, BasePetSittersDataStore(
    apiService
) {
    override fun getPetSitters(serviceType: Int, country: String?, city: String?): LiveData<List<ServiceProvider>> {
        val countryEncoded =
            country?.toByteArray(java.nio.charset.StandardCharsets.UTF_8)?.let { String(it, java.nio.charset.StandardCharsets.ISO_8859_1) }
        val cityEncoded = city?.let {URLEncoder.encode(
            it,
            java.nio.charset.StandardCharsets.UTF_8.toString()
        )  }
        return fetchData { service.getPetSitters(
            serviceType = serviceType,
            country = countryEncoded,
            city = cityEncoded
        ) }
    }

    override fun putLike(serviceId: Int): LiveData<String> {
        return favResponse {service.putLike(serviceId)  }
    }

    override fun deleteLike(serviceId: Int): LiveData<String> {
        return favResponse {service.deleteLike(serviceId)  }
    }

    override fun getFavoriteServices(): LiveData<List<ServiceProvider>> {
        return fetchDataFav { service.getFavoriteServices() }
    }
}