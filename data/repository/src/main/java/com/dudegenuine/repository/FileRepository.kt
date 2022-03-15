package com.dudegenuine.repository

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
    val mapper: IFileDataMapper
): IFileRepository {
    //private val TAG = javaClass.simpleName

    override suspend fun upload(byteArray: ByteArray): File = mapper.asFile(
        service.uploadFile(mapper.asMultipart(File.FILE, byteArray))
    )

    override suspend fun upload(byteArrays: List<ByteArray>): List<File> = mapper.asFiles(
        service.uploadFiles(mapper.asMultiParts(byteArrays))
    )

    override suspend fun download(id: String): File = mapper.asFile(
        service.downloadFile(id)
    )

    override suspend fun delete(id: String) {
        service.deleteFile(id)
    }

    override suspend fun list(): List<File> = mapper.asFiles(
        service.list()
    )

}