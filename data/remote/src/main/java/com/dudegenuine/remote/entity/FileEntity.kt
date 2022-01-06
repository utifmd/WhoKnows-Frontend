package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
data class FileEntity(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("fileDb")
    val fileDb: FileDb,

    @SerializedName("size")
    val size: Long
)

data class FileDb(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("data")
    val data: String
)
