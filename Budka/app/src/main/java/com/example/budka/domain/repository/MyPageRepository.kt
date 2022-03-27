/*
 * *
 *  * Created by Ali Ashkeyev on 21.02.2022, 10:43
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21.02.2022, 10:43
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.Pet
import com.example.budka.data.model.User
import com.example.budka.data.model.UserUpdate
import okhttp3.MultipartBody

interface MyPageRepository {
    fun getProfileByToken():  LiveData<NetworkResult<User>>
    fun updateProfile(image: MultipartBody.Part,
                      body: UserUpdate
    ):  LiveData<NetworkResult<User>>
    fun getUserProfile(userId: Int):  LiveData<NetworkResult<User>>
    fun setRating(userId: Int, rating: Int):  LiveData<NetworkResult<String>>
    fun deleteSession():  LiveData<NetworkResult<String>>
}