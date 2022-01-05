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
    val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileDb

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
