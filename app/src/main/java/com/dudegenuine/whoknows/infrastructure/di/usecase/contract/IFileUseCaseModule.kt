package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.file.UploadFile
import com.dudegenuine.usecase.file.UploadFiles

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileUseCaseModule {
    val uploadFile: UploadFile
    val uploadFiles: UploadFiles
}