/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    val empty = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading = MutableLiveData<Boolean>().apply { value = false }
    val allertMessage = MutableLiveData<String>()
}