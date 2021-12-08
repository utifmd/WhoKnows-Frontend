package com.dudegenuine.whoknows.infrastructure.common

import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.charset.Charset
import kotlin.jvm.Throws

/**
 * Tue, 07 Dec 2021
 * WhoKnows by utifmd
 **/
class BodyConverter<T>(
    private val gson: Gson,
    private val type: Type,
    private val typeAdapter: TypeAdapter<T>
): Converter<ResponseBody, T> {
    // private val TAG: String = javaClass.simpleName

    @Throws(IOException::class)
    override fun convert(body: ResponseBody): T? {
        val source = body.source()

        source.request(Long.MAX_VALUE) // Buffer the entire body.

        return try {
            val buffer = source.buffer
            val json = buffer.clone().readString(Charset.defaultCharset())
            val elem = JsonParser.parseString(json)

            if (elem is JsonObject && elem.has("data"))
                if (elem.get("status").asString != "OK")
                    throw HttpFailureException(elem.get("data").asString)

            gson.fromJson<T>(body.string(), type)
        } catch (e: Exception){
            throw HttpFailureException(e.localizedMessage ?: "Exception.")
        }
    }
}