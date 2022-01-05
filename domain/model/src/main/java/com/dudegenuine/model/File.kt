package com.dudegenuine.model

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
data class File(
    val name: String,
    val url: String,
    val type: String,
    val fileDb: FileDb,
    val size: Long){

    //var isPropsEmpty: Boolean = name.isBlank() && url.isBlank() && type.isBlank() && fileDb.i
}

data class FileDb(
    val id: String,
    val name: String,
    val type: String,
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

