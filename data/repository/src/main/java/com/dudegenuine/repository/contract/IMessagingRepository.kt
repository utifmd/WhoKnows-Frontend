package com.dudegenuine.repository.contract

import com.dudegenuine.model.Messaging
import okhttp3.ResponseBody

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingRepository {
    companion object {
        const val MESSAGING_TOKEN = ""
    }

    suspend fun get(keyName: String): Messaging.Getter.Response
    //suspend fun get(keyName: String): ResponseBody
    suspend fun create(messaging: Messaging): ResponseBody
    suspend fun add(messaging: Messaging): ResponseBody
    suspend fun remove(messaging: Messaging): ResponseBody
    suspend fun push(messaging: Messaging): ResponseBody

    val onMessagingTokenized: () -> String
    val onMessagingTokenRefresh: (String) -> Unit
}