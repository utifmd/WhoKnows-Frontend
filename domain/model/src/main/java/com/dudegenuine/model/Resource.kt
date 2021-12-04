package com.dudegenuine.model

import java.io.IOException

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null ) {

    class Success<T>(data: T): Resource<T> (
        data = data
    )
    class Error<T>(message: String, data: T? = null): Resource<T> (
        message = message,
        data = data
    )
    class Loading<T>(data: T? = null): Resource<T> (
        data = data
    )

    companion object {
        const val IO_EXCEPTION = "Check your internet connection."
        const val HTTP_EXCEPTION = "An expected error occurred."
        const val HTTP_FAILURE_EXCEPTION = "Server response failure."
    }
}
