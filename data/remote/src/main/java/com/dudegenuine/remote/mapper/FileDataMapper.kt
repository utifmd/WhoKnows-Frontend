package com.dudegenuine.remote.mapper

import android.net.Uri
import androidx.core.net.toFile
import com.dudegenuine.model.File
import com.dudegenuine.remote.entity.FileDb
import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IFileDataMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
class FileDataMapper: IFileDataMapper {
    override fun asEntity(): FileEntity {
        TODO("Not yet implemented")
    }

    override fun asEntityFileDb(file: com.dudegenuine.model.FileDb?): FileDb {
        TODO("Not yet implemented")
    }

    override fun asFile(response: Response<FileEntity>): File {
        return File(
            name = response.data?.name ?: "",
            url = response.data?.url ?: "" ,
            type = response.data?.type ?: "" ,
            size = response.data?.size ?: 0 ,
            fileDb = asFileFileDb(response.data?.fileDb)
        )
    }

    override fun asFileFileDb(entity: FileDb?): com.dudegenuine.model.FileDb {
        return com.dudegenuine.model.FileDb(
            id = entity?.id ?: "",
            name = entity?.name ?: "",
            type = entity?.type ?: "",
            data = entity?.data!!,
        )
    }

    override val asMultipart: (Uri) -> MultipartBody.Part = {
        val file = it.toFile()

        val requestFile: RequestBody =
            file.asRequestBody("image/*".toMediaTypeOrNull())

        MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

}