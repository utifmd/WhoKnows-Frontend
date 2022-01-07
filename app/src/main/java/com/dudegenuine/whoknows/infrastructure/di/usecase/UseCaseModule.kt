package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule: IUseCaseModule {
    @Provides
    @ViewModelScoped
    override fun provideUserUseCaseModule(repository: IUserRepository): IUserUseCaseModule {
        return UserUseCase(repository)
    }
}