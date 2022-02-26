/*
 * *
 *  * Created by Ali Ashkeyev on 21.02.2022, 10:50
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21.02.2022, 10:49
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.User
import com.example.budka.data.model.UserUpdate
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.domain.useCase.ProfileUseCase
import okhttp3.MultipartBody
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileViewModel(private val profileUseCase: ProfileUseCase): BaseViewModel() {
    private var profile = MutableLiveData<User>()

    fun fetchProfile(){
        profile = profileUseCase.getProfileByToken() as MutableLiveData<User>
    }

    fun getProfile(): LiveData<User> {
        return profile
    }

    fun updateProfile(image: MultipartBody.Part,
                      body: UserUpdate
    ): LiveData<User>{
        return profileUseCase.updateProfile(image, body)
    }

}