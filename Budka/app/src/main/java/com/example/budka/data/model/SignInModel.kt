/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:18
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:18
 *
 */

package com.example.budka.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("accessToken") var user_token: String,
    var login: String,
    var email: String,
    var message: String?
)

data class SignInModel(
    var username: String,
    var password: String
)

data class LoginData(
    var signIn: SignInModel
)
