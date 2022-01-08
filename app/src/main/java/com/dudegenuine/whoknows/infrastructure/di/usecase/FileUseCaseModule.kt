package com.dudegenuine.whoknows.infrastructure.di.usecase

import android.content.Context
import com.dudegenuine.repository.contract.IFileRepository
import com.dudegenuine.usecase.file.UploadFile
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class FileUseCaseModule(
    private val context: Context,
    private val repository: IFileRepository): IFileUseCaseModule {

    override val uploadFile: UploadFile
        get() = UploadFile(context, repository)
}