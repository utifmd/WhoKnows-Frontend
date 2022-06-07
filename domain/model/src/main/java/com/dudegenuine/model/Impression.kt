package com.dudegenuine.model

/**
 * Mon, 30 May 2022
 * WhoKnows by utifmd
 **/
data class Impression(
    val impressionId: String,
    val postId: String,
    val userId: String,
    val good: Boolean
)
