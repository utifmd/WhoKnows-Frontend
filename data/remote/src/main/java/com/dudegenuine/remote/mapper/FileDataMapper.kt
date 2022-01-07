package com.dudegenuine.remote.mapper

import android.content.Context
import com.dudegenuine.model.File
import com.dudegenuine.remote.entity.FileEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IFileDataMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import javax.inject.Inject

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
class FileDataMapper

    @Inject constructor(
    val context: Context): IFileDataMapper {

    override fun asEntity(file: File): FileEntity {
        TODO("Not yet implemented")
    }

    override fun asFile(entity: FileEntity): File {
        return File(
            id = entity.fileDb.id,
            name = entity.name,
            url = entity.url,
            type = entity.type,
            size = entity.size,
            data = entity.fileDb.data
        )
    }

    override fun asFile(response: Response<FileEntity>): File {
        return when(response.data){
            is FileEntity -> asFile(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asFiles(response: Response<List<FileEntity>>): List<File> {
        return when(response.data){
            is List<*> -> {
                val entities = response.data.filterIsInstance<FileEntity>()

                entities.map { asFile(it) }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun asMultiParts(byteArrays: List<ByteArray>): List<MultipartBody.Part> =
        byteArrays.map { asMultipart(it) }

    override fun asMultipart(byteArray: ByteArray): MultipartBody.Part {
        val contentPart = byteArray
            .toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)

        return MultipartBody.Part
            .createFormData("file", "file-${Date().time}", contentPart)
    }

    /*val resolver: ContentResolver = context.contentResolver
    val contentPart: RequestBody = ContentUriRequestBody(resolver, uri) // file.asRequestBody("image/*".toMediaTypeOrNull())

    /*val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", "file-${Date().time}", contentPart)
        .build()*/

    /*val request = Request.Builder()
        .url(serverUrl)
        .post(requestBody)
        .build()*/

    return MultipartBody.Part.create(requestBody)
    internal class ContentUriRequestBody(
        private val contentResolver: ContentResolver,
        private val uri: Uri): RequestBody() {

        override fun contentLength(): Long = -1

        override fun contentType(): MediaType? {
            val contentType = contentResolver.getType(uri)
                ?: return null

            return contentType.toMediaTypeOrNull()
        }

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            val inputStream = contentResolver.openInputStream(uri)
                ?: throw IOException("Couldn't open content Uri for reading: $uri")

            inputStream.source().use {
                sink.writeAll(it)
            }
        }
    */ */
}