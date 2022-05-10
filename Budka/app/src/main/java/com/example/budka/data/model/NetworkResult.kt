/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:49
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:49
 *
 */

package com.example.budka.data.model

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    var show: Boolean = false
) {

    class Success<T>(data: T?) : NetworkResult<T>(data)

    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)

    class Loading<T> : NetworkResult<T>(show = true)




}


inline fun <reified T> NetworkResult<T>.doIfFailure(callback: (error: String?, data: T?) -> Unit) {
    if (this is NetworkResult.Error) {
        callback(message, data)
    }
}

inline fun <reified T> NetworkResult<T>.doIfSuccess(callback: (data: T?) -> Unit) {
    if (this is NetworkResult.Success) {
        callback(data)
    }
}

inline fun <reified T> NetworkResult<T>.doIfLoading(callback: (show: Boolean) -> Unit) {
    if (this is NetworkResult.Loading) {
        this.show = true
        callback(true)
    }
}

inline fun <reified T, reified R>  NetworkResult<T>.map(transform: (T) -> R):  NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(data?.let { transform(it) })
        is NetworkResult.Error -> NetworkResult.Error(message)
        else -> NetworkResult.Error(message)
    }
}


