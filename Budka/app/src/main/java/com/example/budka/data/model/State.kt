/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:49
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:49
 *
 */

package com.example.budka.data.model

sealed class Statesealed {
    object ShowLoading: Statesealed()
    object HideLoading: Statesealed()
    object FailedLoading: Statesealed()
    object Result: Statesealed()
}
