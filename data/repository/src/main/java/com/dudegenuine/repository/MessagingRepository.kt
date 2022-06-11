package com.dudegenuine.repository

import android.util.Log
import com.dudegenuine.model.Messaging
import com.dudegenuine.remote.mapper.contract.IMessagingDataMapper
import com.dudegenuine.remote.service.contract.IMessagingService
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MessagingRepository
    @Inject constructor(
    private val service: IMessagingService,
    private val mapper: IMessagingDataMapper,
    override val receiver: IReceiverFactory,
    override val preference: IPrefsFactory,
    override val firebase: IFirebaseManager,
    override val workerManager: IWorkerManager): IMessagingRepository {
    private val TAG: String = javaClass.simpleName

    override suspend fun readFlow(keyName: String) =
        flowOf(get(keyName)).onStart { emit(Messaging.Getter.Response("")) }

    override suspend fun removeFlow(keyName: String, key: String) =
        flowOf(remove(Messaging.GroupRemover(keyName, listOf(preference.tokenId), key)))
            .mapLatest { key }

    override suspend fun createFlow(keyName: String): Flow<String> =
        flowOf(create(Messaging.GroupCreator(keyName, listOf(preference.tokenId))))
            .mapLatest { keyName }

    override suspend fun addFlow(keyName: String, key: String): Flow<String> =
        flowOf(add(Messaging.GroupAdder(keyName, listOf(preference.tokenId), key)))
            .mapLatest { key }

    override suspend fun pushFlow(messaging: Messaging.Pusher): Flow<String> =
        flowOf(push(messaging)).mapLatest { messaging.to }

    override suspend fun registerGroupTokenFlow(keyName: String): Flow<String> = readFlow(keyName)
        .flatMapConcat { response -> if (response.notification_key.isNotBlank())
            addFlow(keyName, response.notification_key) else
                createFlow(keyName)
        }

        /*{
        readFlow(keyName).collectLatest { notification_key ->
            Log.d(TAG, "registerGroupTokenFlow: $notification_key")
            if (notification_key.isNotBlank()) addFlow(keyName, notification_key).collect()
            else createFlow(keyName).collect()
        }

        return flow { emit("registered") }
    }*/
        /*.onStart { emit("") }
        .onEach{ if(it.isNotBlank())
            addFlow(keyName, it).collect() else
                createFlow(keyName).collect()
        }
        .mapLatest { "registered" }*/

    /*.flatMapConcat{ if (it.isNotBlank())
            addFlow(keyName, it) else
                createFlow(keyName) }
        .retryWhen{ cause, attempt -> cause is IOException || attempt < 3 }*/

    override suspend fun unregisterGroupTokenFlow(keyName: String): Flow<String> = readFlow(keyName)
        .onEach { response ->
            Log.d(TAG, "unregisterGroupTokenFlow: $response")
            if (response.notification_key.isNotBlank())
                removeFlow(keyName, response.notification_key).collect()
            //else createFlow(keyName).collect()
        }.mapLatest { "registered".plus(keyName) }
        /*= readFlow(keyName)
        //.flatMapConcat{ removeFlow(it, keyName) }
        .onStart { emit("") }
        .onEach{ if(it.isNotBlank()) removeFlow(it, keyName).collect() }
        .mapLatest { "unregistering" }*/
        //.retryWhen{ cause, attempt -> cause is IOException || attempt < 3 }

    override suspend fun get(keyName: String): Messaging.Getter.Response {
        return mapper.asMessagingGetterResponse(
            service.get(keyName)
        )
    }
    override suspend fun create(messaging: Messaging): ResponseBody {
        return mapper.asResponseBody(
            service.create(mapper.asMessagingCreateEntity(messaging))
        )
    }
    override suspend fun add(messaging: Messaging): ResponseBody {
        return mapper.asResponseBody(
            service.add(mapper.asMessagingAddEntity(messaging))
        )
    }
    override suspend fun remove(messaging: Messaging): ResponseBody {
        return mapper.asResponseBody(
            service.remove(mapper.asMessagingRemoveEntity(messaging))
        )
    }
    override suspend fun push(messaging: Messaging): ResponseBody {
        return mapper.asResponseBody(
            service.push(mapper.asMessagingPushEntity(messaging))
        )
    }
}