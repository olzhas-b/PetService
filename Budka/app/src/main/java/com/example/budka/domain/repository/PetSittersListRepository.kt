/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.*

interface PetSittersListRepository {
    fun getPetSitters(serviceType: Int, country: String?, city: String?, petType: String?):  LiveData<NetworkResult<ServiceProviderResponse>>
    fun putLike(serviceId: Int):  LiveData<NetworkResult<String>>
    fun deleteLike(serviceId: Int):  LiveData<NetworkResult<String>>
    fun getFavoriteServices():  LiveData<NetworkResult<List<ServiceProvider>>>

}