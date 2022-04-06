package com.dudegenuine.repository

import android.content.BroadcastReceiver
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.model.Messaging
import com.dudegenuine.remote.mapper.contract.IMessagingDataMapper
import com.dudegenuine.remote.service.contract.IMessagingService
import com.dudegenuine.repository.contract.IMessagingRepository
import okhttp3.ResponseBody

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class MessagingRepository(
    private val service: IMessagingService,
    private val mapper: IMessagingDataMapper,
    receiver: IReceiverFactory): IMessagingRepository {

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

    override val onNetworkReceived: (onConnected: (String) -> Unit) ->
        BroadcastReceiver = receiver.networkReceived

    override val onTokenReceived: (onTokenized: (String) -> Unit) ->
        BroadcastReceiver = receiver.tokenReceived
}