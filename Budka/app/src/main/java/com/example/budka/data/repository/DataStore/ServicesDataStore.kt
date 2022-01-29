/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.repository.DataStore

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.Pet
import com.example.budka.data.model.Properties
import com.example.budka.data.model.Services
import com.example.budka.data.repository.Base.BasePetsDataStore
import com.example.budka.data.repository.Base.BaseServicesDataStore
import com.example.budka.domain.repository.PetsListRepository
import com.example.budka.domain.repository.ServicesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServicesDataStore(apiService: ApiService): ServicesRepository, BaseServicesDataStore(apiService) {

    override fun getUserServices(user_id: Int): LiveData<List<Services>> {
        return fetchData { service.getUserServices(user_id) }
    }

    override fun createService(
        images: List<MultipartBody.Part>,
        partMap: Map<String, RequestBody>,
        properties: List<Properties>,
    ): LiveData<CreateServiceModel> {
        return postService {
            Log.d("image", images.toString())

            service.createService( images, partMap, properties) }
    }
}