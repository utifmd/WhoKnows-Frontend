package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.IFileRepository
import com.dudegenuine.usecase.file.UploadFile

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileUseCaseModule {
    fun provideUploadFile(repos: IFileRepository): UploadFile
}