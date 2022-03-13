package com.dudegenuine.whoknows.infrastructure.di.network

import com.dudegenuine.whoknows.infrastructure.di.network.contract.INetworkModule
import com.dudegenuine.whoknows.infrastructure.di.network.contract.INetworkModule.Companion.CONNECT_TIMEOUT
import com.dudegenuine.whoknows.infrastructure.di.network.contract.INetworkModule.Companion.READ_TIMEOUT
import com.dudegenuine.whoknows.infrastructure.di.network.contract.INetworkModule.Companion.WRITE_TIMEOUT
import com.dudegenuine.whoknows.infrastructure.di.network.factory.BodyFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule: INetworkModule {

    @Provides
    @Singleton
    override fun provideGson(): Gson = GsonBuilder().create()
        // .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)

    @Provides
    @Singleton
    override fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            // addInterceptor(RespInterceptor())
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
        }.build()
    }

    @Provides
    @Singleton
    override fun provideNetwork(gson: Gson, client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(BodyFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }
}