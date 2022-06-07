package com.dudegenuine.repository

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
import java.io.IOException
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
        override val workerManager: IWorkerManager
    ): IMessagingRepository {

    override suspend fun readFlow(keyName: String) =
        flowOf(get(keyName).notification_key)

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
        .flatMapConcat{ if (it.isNotBlank()) addFlow(keyName, it)
            else createFlow(keyName) }
        .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }

    override suspend fun unregisterGroupTokenFlow(keyName: String) = readFlow(keyName)
        .flatMapConcat{ removeFlow(it, keyName) }
        .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }

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