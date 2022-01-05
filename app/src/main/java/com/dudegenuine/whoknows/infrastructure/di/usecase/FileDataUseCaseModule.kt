package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IFileRepository
import com.dudegenuine.usecase.file.UploadFile
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object FileDataUseCaseModule: IFileUseCaseModule {
    @Provides
    @ViewModelScoped
    override fun provideUploadFile(repos: IFileRepository): UploadFile {
        return UploadFile(repos)
    }
}