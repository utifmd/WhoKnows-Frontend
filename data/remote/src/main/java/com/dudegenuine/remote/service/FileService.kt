package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IFileService
import com.dudegenuine.remote.service.contract.IFileService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IFileService.Companion.ENDPOINT
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface FileService: IFileService {
    @Multipart
    @Headers(API_KEY)
    @POST(ENDPOINT)
    override suspend fun upload(
        @Part file: MultipartBody.Part): Response<FileEntity>
}