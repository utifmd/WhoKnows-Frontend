package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.Messaging
import com.dudegenuine.remote.entity.MessagingAddEntity
import com.dudegenuine.remote.entity.MessagingCreateEntity
import com.dudegenuine.remote.entity.MessagingPushEntity
import com.dudegenuine.remote.entity.MessagingRemoveEntity
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingDataMapper {
    fun asMessagingPushEntity(messaging: Messaging): MessagingPushEntity
    fun asMessagingCreateEntity(messaging: Messaging): MessagingCreateEntity
    fun asMessagingAddEntity(messaging: Messaging): MessagingAddEntity
    fun asMessagingRemoveEntity(messaging: Messaging): MessagingRemoveEntity
    fun asResponseBody(response: Response<ResponseBody>): ResponseBody
    fun asMessagingGetterResponse(response: Response<ResponseBody>): Messaging.Getter.Response
}