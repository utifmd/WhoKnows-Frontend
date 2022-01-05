package com.dudegenuine.remote.mapper.contract

import android.net.Uri
import com.dudegenuine.model.File
import com.dudegenuine.model.FileDb
import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import okhttp3.MultipartBody

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileDataMapper {
    fun asEntity (): FileEntity
    fun asFile(response: Response<FileEntity>): File
    fun asEntityFileDb(file: FileDb?): com.dudegenuine.remote.entity.FileDb
    fun asFileFileDb(entity: com.dudegenuine.remote.entity.FileDb?): FileDb

    val asMultipart: (Uri) -> MultipartBody.Part
    //fun asFileDb (): FileEntity
}