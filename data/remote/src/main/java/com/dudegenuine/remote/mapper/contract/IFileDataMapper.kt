package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.File
import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import okhttp3.MultipartBody

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileDataMapper {
    fun asEntity(file: File): FileEntity
    fun asFile(entity: FileEntity): File
    fun asFile(response: Response<FileEntity>): File
    fun asFiles(response: Response<List<FileEntity>>): List<File>
    fun asMultipart(paramName: String, byteArray: ByteArray): MultipartBody.Part
    fun asMultiParts(byteArrays: List<ByteArray>): List<MultipartBody.Part>
}