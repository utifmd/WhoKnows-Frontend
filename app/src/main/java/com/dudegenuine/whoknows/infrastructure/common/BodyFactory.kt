package com.dudegenuine.whoknows.infrastructure.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.NullPointerException
import java.lang.reflect.Type

/**
 * Tue, 07 Dec 2021
 * WhoKnows by utifmd
 **/
class BodyFactory(
    private val gson: Gson): Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))

        return BodyConverter(gson, type, adapter)
    }

    companion object {
        fun create() = BodyFactory(Gson())
        fun create(gson: Gson?): BodyFactory {
            if (gson == null){
                throw NullPointerException("gson is null.")
            }

            return BodyFactory(gson)
        }
    }
}