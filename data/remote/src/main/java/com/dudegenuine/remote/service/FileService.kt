package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IFileService
import com.dudegenuine.remote.service.contract.IFileService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IFileService.Companion.API_KEY
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface FileService: IFileService {
    @POST("/upload-file")
    @Multipart
    @Headers(API_KEY)
    override suspend fun uploadFile(
        @Part file: MultipartBody.Part): Response<FileEntity>

    override suspend fun downloadFile(id: String): Response<FileEntity>

    @POST("/upload-files")
    @Multipart
    @Headers(API_KEY)
    override suspend fun uploadFiles(
        @Part files: List<MultipartBody.Part>): Response<List<FileEntity>>

    @DELETE("/files/{id}")
    @Headers(API_KEY, ACCEPT)
    override suspend fun deleteFile(id: String)

    override suspend fun list(): Response<List<FileEntity>>
}