/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.api

import android.content.SharedPreferences
import android.util.Log
import com.example.budka.data.model.SessionManager
import com.example.budka.utils.Constants.Companion.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient { fun create(okHttpClient: OkHttpClient): ApiService {
    return Retrofit.Builder()
        .baseUrl("http://192.168.43.226:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()
        .create(ApiService::class.java)
}

    fun getOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        val logginInterceptor = HttpLoggingInterceptor()
        logginInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        return builder.addInterceptor(logginInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(authInterceptor)
            .build()
    }

    fun getAuthInterceptor(sessionManager: SessionManager): Interceptor {
        return Interceptor { chain ->
            val newRequest = chain.request()
                .newBuilder()
//                .addHeader("Bearer ", sharedPreferences.getString("token", "")!!)
            Log.d("mytoken",  sessionManager.fetchSessionId().toString())
            sessionManager.fetchSessionId()?.let{result->
                newRequest.header("Authorization", "Bearer $result") }
            chain.proceed(newRequest.build())
        }
    }
}