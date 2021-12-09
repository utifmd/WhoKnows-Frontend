package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.ResultRepository
import com.dudegenuine.usecase.result.*

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultUseCaseModule {
    fun providePostResultModule(repos: ResultRepository): PostResult
    fun provideGetResultModule(repos: ResultRepository): GetResult
    fun providePatchResultModule(repos: ResultRepository): PatchResult
    fun provideDeleteResultModule(repos: ResultRepository): DeleteResult
    fun provideGetResultsModule(repos: ResultRepository): GetResults
}