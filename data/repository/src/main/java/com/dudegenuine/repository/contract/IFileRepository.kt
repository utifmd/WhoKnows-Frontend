package com.dudegenuine.repository.contract

import com.dudegenuine.model.File

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileRepository {
    suspend fun upload(byteArray: ByteArray): File
    suspend fun download(id: String): File
    suspend fun upload(byteArrays: List<ByteArray>): List<File>
    suspend fun delete(id: String)
    suspend fun list(): List<File>

    companion object {
        const val NOT_FOUND = "File not found."
    }
}