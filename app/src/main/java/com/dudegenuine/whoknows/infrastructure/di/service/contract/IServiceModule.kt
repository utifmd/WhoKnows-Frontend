package com.dudegenuine.whoknows.infrastructure.di.service.contract

import com.dudegenuine.remote.service.contract.*
import retrofit2.Retrofit

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IServiceModule {
    fun provideUserService(network: Retrofit.Builder): IUserService
    fun provideRoomService(network: Retrofit.Builder): IRoomService
    fun provideQuizService(network: Retrofit.Builder): IQuizService
    fun provideParticipantService(network: Retrofit.Builder): IParticipantService
    fun provideResultService(network: Retrofit.Builder): IResultService
    fun provideFileService(network: Retrofit.Builder): IFileService
    fun provideNotificationService(network: Retrofit.Builder): INotificationService
    fun providePushNotificationService(network: Retrofit.Builder): IPushNotificationService
}