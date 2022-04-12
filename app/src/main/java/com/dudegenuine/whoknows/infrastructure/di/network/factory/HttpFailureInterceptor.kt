package com.dudegenuine.whoknows.infrastructure.di.network.factory

import android.util.Log
import com.dudegenuine.model.Resource
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Sat, 09 Apr 2022
 * WhoKnows by utifmd
 **/
class HttpFailureInterceptor: Interceptor {
    private val TAG: String = javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return try {
            val bodyString = response.body?.string() // ?: throw IllegalStateException(response.message) //Log.d(TAG, "interceptor passed")

            response.newBuilder()
                .body(bodyString?.toResponseBody(response.body?.contentType()))
                .build()

        } catch (e: Exception) {
            val message = when (e) {
                is SocketTimeoutException -> Resource.SOCKET_TIMEOUT_EXCEPTION
                is UnknownHostException -> Resource.UNKNOWN_HOST_EXCEPTION
                is ConnectionShutdownException -> Resource.CONN_SHUT_DOWN_EXCEPTION
                is IOException -> Resource.IO_EXCEPTION
                is IllegalStateException -> Resource.ILLEGAL_STATE_EXCEPTION
                else -> e.message ?: e.localizedMessage ?: Resource.HTTP_EXCEPTION
            }

            Log.d(TAG, "Exception: ${e.message}")
            Response.Builder()
                .request(request)
                .protocol(response.protocol)
                .code(response.code)
                .message(response.message)
                .body(message.toResponseBody())
                .build()
            //response
        }

        /*if (!response.isSuccessful)
            Log.d(TAG, "intercept: ${response.message}")
        //throw HttpFailureException(response.message)
        return response*/
    }
}