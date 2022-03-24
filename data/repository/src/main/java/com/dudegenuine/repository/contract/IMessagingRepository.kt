package com.dudegenuine.repository.contract

import android.content.BroadcastReceiver
import com.dudegenuine.model.Messaging
import okhttp3.ResponseBody

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingRepository {
    companion object {
        const val MESSAGING_TOKEN = "current_user_firebase_messaging_token"
    }

    suspend fun get(keyName: String): Messaging.Getter.Response
    //suspend fun get(keyName: String): ResponseBody
    suspend fun create(messaging: Messaging): ResponseBody
    suspend fun add(messaging: Messaging): ResponseBody
    suspend fun remove(messaging: Messaging): ResponseBody
    suspend fun push(messaging: Messaging): ResponseBody

    val currentToken: () -> String //currentToken
    val onTokenRefresh: (String) -> Unit //onTokenRefresh

    val currentBadgeStatus: () -> Boolean
    val onBadgeStatusRefresh: (Boolean) -> Unit

    val onNetworkReceived: (onConnected: (String) -> Unit) -> BroadcastReceiver
    val onTokenReceived: (onTokenized: (String) -> Unit) -> BroadcastReceiver
}