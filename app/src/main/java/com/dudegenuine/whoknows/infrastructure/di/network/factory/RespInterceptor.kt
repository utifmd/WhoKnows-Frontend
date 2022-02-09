package com.dudegenuine.whoknows.infrastructure.di.network.factory

import com.dudegenuine.model.common.validation.HttpFailureException
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RespInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val body = response.body ?: return response
        val source = body.source()

        try {
            val buffer = source.buffer
            val json = buffer.clone().readString(Charset.defaultCharset())
            val elem = JsonParser.parseString(json)

            if (elem is JsonObject && elem.has("data"))
                if (elem.get("status").asString != "OK")
                    throw HttpFailureException(elem.get("data").asString)

        } catch (e: Exception){
            throw HttpFailureException(e.localizedMessage ?: "Exception.")
        }

        return response
    }
}