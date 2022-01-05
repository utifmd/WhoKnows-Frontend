package com.dudegenuine.repository.contract

import android.net.Uri
import com.dudegenuine.model.File

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFileRepository {
    suspend fun upload(uri: Uri): File
    suspend fun download(id: String): File
    suspend fun list(): List<File>
}