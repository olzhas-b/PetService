/*
 * *
 *  * Created by Ali Ashkeyev on 05.02.2022, 15:11
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 05.02.2022, 15:11
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.Properties
import com.example.budka.data.model.UploadImage
import com.example.budka.view.PropertiesList

class createServiceViewModel: ViewModel() {
    val propertiesList = MutableLiveData<List<Properties>>()
    val imageList = MutableLiveData<List<UploadImage>>()

    fun getProperties(): LiveData<List<Properties>> {
        return propertiesList
    }
    fun getImages(): LiveData<List<UploadImage>> {
        return imageList
    }

}