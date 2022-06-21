package com.dudegenuine.repository.contract

import com.dudegenuine.model.Messaging
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingRepository {
    companion object {
        const val MESSAGING_TOKEN = "current_user_firebase_messaging_token"
    }

    //suspend fun get(keyName: String): ResponseBody
    suspend fun get(keyName: String): Messaging.Getter.Response
    suspend fun create(messaging: Messaging): ResponseBody
    suspend fun create(messaging: Messaging.GroupCreator): Messaging.Getter.Response
    suspend fun add(messaging: Messaging): ResponseBody
    suspend fun add(messaging: Messaging.GroupAdder): Messaging.Getter.Response
    suspend fun remove(messaging: Messaging): ResponseBody
    suspend fun push(messaging: Messaging): ResponseBody

    suspend fun readFlow(keyName: String): Flow<Messaging.Getter.Response>
    suspend fun removeFlow(keyName: String, key: String): Flow<String>
    suspend fun createFlow(keyName: String): Flow<String>
    suspend fun addFlow(keyName: String, key: String): Flow<String>
    suspend fun pushFlow(messaging: Messaging.Pusher): Flow<String>
    suspend fun registerGroupTokenFlow(keyName: String): Flow<String>
    suspend fun unregisterGroupTokenFlow(keyName: String): Flow<String>

    val receiver: IReceiverFactory
    val preference: IPrefsFactory
    val firebase: IFirebaseManager
    val workerManager: IWorkerManager
}