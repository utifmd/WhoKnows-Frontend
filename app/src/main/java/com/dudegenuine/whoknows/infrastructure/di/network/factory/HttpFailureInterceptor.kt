package com.dudegenuine.whoknows.infrastructure.di.network.factory

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Sat, 09 Apr 2022
 * WhoKnows by utifmd
 **/
class HttpFailureInterceptor: Interceptor {
    private val TAG: String = javaClass.simpleName
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful)
            Log.d(TAG, "intercept: ${response.message}")
        //throw HttpFailureException(response.message)
        return response
    }
}