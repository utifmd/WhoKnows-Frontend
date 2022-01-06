package com.dudegenuine.model

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
data class File(
    val id: String,
    val name: String,
    val url: String,
    val type: String,
    val data: String,
    val size: Long){

    //var isPropsEmpty: Boolean = name.isBlank() && url.isBlank() && type.isBlank() && fileDb.i
}
