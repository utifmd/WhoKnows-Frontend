package com.dudegenuine.model

/**
 * Tue, 01 Mar 2022
 * WhoKnows by utifmd
 **/
data class RoomCensored(
    val roomId: String,
    val userId: String,
    val minute: Int,
    val title: String,
    val description: String,
    val expired: Boolean,
)
