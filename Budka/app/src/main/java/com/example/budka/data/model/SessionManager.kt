/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:35
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:35
 *
 */

package com.example.budka.data.model

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "usertoken"
        const val EMAIL = "email"
        const val USERNAME = "username"
    }

    /**
     * Function to save auth token
     */
    fun saveLoginData(loginResponse: LoginResponse) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, loginResponse.user_token)
        editor.putString(EMAIL, loginResponse.email)
        editor.putString(USERNAME, loginResponse.login)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchSessionId(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun fetchUsername(): String? {
        return prefs.getString(USERNAME, null)
    }
    fun fetchEmail(): String? {
        return prefs.getString(EMAIL, null)
    }

    fun deleteSession(){
        prefs.edit().clear().apply()
    }
}