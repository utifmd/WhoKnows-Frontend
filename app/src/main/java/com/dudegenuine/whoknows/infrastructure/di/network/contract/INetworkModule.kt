package com.dudegenuine.whoknows.infrastructure.di.network.contract

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface INetworkModule {
    fun provideGson(): Gson
    fun provideClient(): OkHttpClient
    fun provideNetwork(gson: Gson, client: OkHttpClient): Retrofit.Builder

    companion object {
        const val CONNECT_TIMEOUT = 60L //10L
        const val WRITE_TIMEOUT = 60L //1L
        const val READ_TIMEOUT = 60L //20L
    }
}