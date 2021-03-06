/*
 * *
 *  * Created by Ali Ashkeyev on 21.02.2022, 10:48
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21.02.2022, 10:48
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.User
import com.example.budka.data.model.UserUpdate
import com.example.budka.domain.repository.MyPageRepository
import okhttp3.MultipartBody

class ProfileUseCase (val myPageRepository: MyPageRepository){
    fun getProfileByToken(): LiveData<NetworkResult<User>> {
        return myPageRepository.getProfileByToken()
    }
    fun updateProfile(image: MultipartBody.Part,
                      body: UserUpdate
    ): LiveData<NetworkResult<User>>{
        return myPageRepository.updateProfile(image, body)
    }
    fun getUserProfile(userId: Int): LiveData<NetworkResult<User>> {
        return myPageRepository.getUserProfile(userId)
    }

    fun setRating(userId: Int, rating: Int): LiveData<NetworkResult<String>> {
        return myPageRepository.setRating(userId, rating)
    }

    fun deleteSession(): LiveData<NetworkResult<String>>{
        return myPageRepository.deleteSession()
    }

    fun verifyPhone(): LiveData<NetworkResult<String>>{
        return myPageRepository.verifyPhone()
    }
}