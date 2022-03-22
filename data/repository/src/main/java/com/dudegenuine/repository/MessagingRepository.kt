package com.dudegenuine.repository

import android.content.BroadcastReceiver
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_NOTIFICATION_BADGE_STATUS
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.model.Messaging
import com.dudegenuine.remote.mapper.contract.IMessagingDataMapper
import com.dudegenuine.remote.service.contract.IMessagingService
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IMessagingRepository.Companion.MESSAGING_TOKEN
import okhttp3.ResponseBody

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class MessagingRepository(
    private val service: IMessagingService,
    private val mapper: IMessagingDataMapper,
    private val pref: IPreferenceManager,
    private val receiver: IReceiverFactory): IMessagingRepository {
    private val TAG: String = javaClass.simpleName

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

    override val onMessagingTokenized: () ->
        String = { pref.readString(MESSAGING_TOKEN) }

    override val onMessagingTokenRefresh: (String) ->
        Unit = { pref.write(MESSAGING_TOKEN, it) }

    override val currentBadgeStatus: () ->
        Boolean = { pref.readBoolean(CURRENT_NOTIFICATION_BADGE_STATUS) }

    override val onBadgeStatusRefresh: (Boolean) ->
        Unit = { pref.write(CURRENT_NOTIFICATION_BADGE_STATUS, it) }
}