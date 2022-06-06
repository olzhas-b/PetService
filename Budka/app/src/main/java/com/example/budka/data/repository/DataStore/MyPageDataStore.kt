/*
 * *
 *  * Created by Ali Ashkeyev on 21.02.2022, 10:41
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21.02.2022, 10:41
 *
 */

package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.CreateServiceModel
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.User
import com.example.budka.data.model.UserUpdate
import com.example.budka.data.repository.Base.BaseProfileDataStore
import com.example.budka.domain.repository.MyPageRepository
import okhttp3.MultipartBody

class MyPageDataStore (apiService: ApiService) :  BaseProfileDataStore(apiService), MyPageRepository {
    override fun getProfileByToken():  LiveData<NetworkResult<User>> {
        return profileByTokenResponse { service.getProfileByToken() }
    }

    override fun updateProfile(
        image: MultipartBody.Part,
        body: UserUpdate
    ):  LiveData<NetworkResult<User>> {
        return profileByTokenResponse { service.updateProfile(image, body) }
    }

    override fun getUserProfile(userId: Int):  LiveData<NetworkResult<User>> {
        return profileByTokenResponse { service.getUserProfile(userId) }
    }

    override fun setRating(userId: Int, rating: Int):  LiveData<NetworkResult<String>> {
        return deleteResponse { service.setRating(userId, rating) }
    }

    override fun deleteSession(): LiveData<NetworkResult<String>> {
        return deleteResponse { service.deleteSession("https://auth-pet-service.herokuapp.com/api/v1/user/sign-out") }
    }

    override fun verifyPhone(): LiveData<NetworkResult<String>> {
        return deleteResponse { service.verifyPhone() }
    }
}