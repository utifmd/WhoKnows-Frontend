package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import okhttp3.MultipartBody

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileService {
    suspend fun upload(file: MultipartBody.Part): Response<FileEntity>
    suspend fun download(id: String): Response<FileEntity>
    suspend fun list(): Response<List<FileEntity>>
    //suspend fun list(listRequest: ListRequest): Response<List<FileEntity>>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev" //const val CONTENT_TYPE = "Content-Type: application/json" //const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/upload"
    }
}