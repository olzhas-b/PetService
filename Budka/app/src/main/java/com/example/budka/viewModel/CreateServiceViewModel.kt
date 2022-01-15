/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 20:40
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 20:40
 *
 */

package com.example.budka.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.ServiceRequiredField
import com.example.budka.data.model.UploadImage
import com.example.budka.domain.useCase.PetSittersListUseCase

class CreateServiceViewModel (): BaseViewModel() {
    var requiredData = MutableLiveData<ServiceRequiredField>()

    fun setRequiredData(data: ServiceRequiredField){
        Log.d("myData", data.toString())
        requiredData.value = data
    }

}