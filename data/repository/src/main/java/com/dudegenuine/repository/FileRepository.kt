package com.dudegenuine.repository

import com.dudegenuine.model.File
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IFileDataMapper
import com.dudegenuine.remote.service.contract.IFileService
import com.dudegenuine.repository.contract.IFileRepository
import com.dudegenuine.repository.contract.IFileRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
class FileRepository

@Inject constructor(
    val service: IFileService,
    val mapper: IFileDataMapper): IFileRepository {
    //private val TAG = javaClass.simpleName

    override suspend fun upload(byteArray: ByteArray): File = try {
        mapper.asFile(
            service.uploadFile(mapper.asMultipart(byteArray))
        )
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun upload(byteArrays: List<ByteArray>): List<File> = try {
        mapper.asFiles(
            service.uploadFiles(mapper.asMultiParts(byteArrays))
        )
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun download(id: String): File = try {
        mapper.asFile(
            service.downloadFile(id)
        )
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) = try {
        service.deleteFile(id)
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun list(): List<File> = try {
        mapper.asFiles(
            service.list()
        )
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

}