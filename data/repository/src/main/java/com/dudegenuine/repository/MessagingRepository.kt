package com.dudegenuine.repository

import com.dudegenuine.local.api.IPreferenceManager
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
    private val pref: IPreferenceManager): IMessagingRepository {
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

    override suspend fun push(messaging: Messaging): ResponseBody {
        return mapper.asResponseBody(
            service.push(mapper.asMessagingPushEntity(messaging))
        )
    }

    override val onMessagingTokenized: () -> String =
        { pref.read(MESSAGING_TOKEN) }

    override val onMessagingTokenRefresh: (String) -> Unit =
        { pref.write(MESSAGING_TOKEN, it) }
}