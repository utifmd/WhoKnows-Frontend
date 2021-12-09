package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.ResultRepository
import com.dudegenuine.usecase.result.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IResultUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/

@Module
@InstallIn(ViewModelComponent::class)
object ResultUseCaseModule: IResultUseCaseModule {

    @Provides
    @ViewModelScoped
    override fun providePostResultModule(repos: ResultRepository): PostResult {
        return PostResult(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetResultModule(repos: ResultRepository): GetResult {
        return GetResult(repos)
    }

    @Provides
    @ViewModelScoped
    override fun providePatchResultModule(repos: ResultRepository): PatchResult {
        return PatchResult(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideDeleteResultModule(repos: ResultRepository): DeleteResult {
        return DeleteResult(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetResultsModule(repos: ResultRepository): GetResults {
        return GetResults(repos)
    }
}