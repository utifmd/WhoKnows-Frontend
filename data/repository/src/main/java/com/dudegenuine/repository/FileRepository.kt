package com.dudegenuine.repository

import android.net.Uri
import com.dudegenuine.model.File
import com.dudegenuine.remote.mapper.contract.IFileDataMapper
import com.dudegenuine.remote.service.contract.IFileService
import com.dudegenuine.repository.contract.IFileRepository
import javax.inject.Inject

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
class FileRepository

@Inject constructor(
    val service: IFileService,
    val mapper: IFileDataMapper): IFileRepository {

    override suspend fun upload(uri: Uri): File {
        return mapper.asFile(
            service.upload(mapper.asMultipart(uri))
        )
    }

    override suspend fun download(id: String): File {
        TODO("Not yet implemented")
    }

    override suspend fun list(): List<File> {
        TODO("Not yet implemented")
    }

}