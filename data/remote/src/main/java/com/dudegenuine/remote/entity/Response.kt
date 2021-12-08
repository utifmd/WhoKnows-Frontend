package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Response<T> (

    @SerializedName("code")
    val code: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: T? = null
)