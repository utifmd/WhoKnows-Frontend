package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.entity.MessagingAddEntity
import com.dudegenuine.remote.entity.MessagingCreateEntity
import com.dudegenuine.remote.entity.MessagingPushEntity
import com.dudegenuine.remote.entity.MessagingRemoveEntity
import com.dudegenuine.remote.mapper.contract.IMessagingDataMapper
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class MessagingDataMapper
    @Inject constructor(val gson: Gson): IMessagingDataMapper {
    private val TAG: String = javaClass.simpleName

    override fun asMessagingPushEntity(messaging: Messaging): MessagingPushEntity {
        return when (messaging){
            is Messaging.Pusher -> MessagingPushEntity(
                data = messaging.copy(),
                to = messaging.to)
            else -> throw IllegalStateException()
        }
    }

    override fun asMessagingCreateEntity(messaging: Messaging): MessagingCreateEntity {
        return when (messaging){
            is Messaging.GroupCreator -> MessagingCreateEntity(
                operation = messaging.operation,
                notificationKeyName = messaging.keyName,
                registrationIds = messaging.tokens)
            else -> throw IllegalStateException()
        }
    }

    override fun asMessagingAddEntity(messaging: Messaging): MessagingAddEntity {
        return when (messaging){
            is Messaging.GroupAdder -> MessagingAddEntity(
                operation = messaging.operation,
                notificationKeyName = messaging.keyName,
                notificationKey = messaging.key,
                registrationIds = messaging.tokens)

            else -> throw IllegalStateException()
        }
    }

    override fun asMessagingRemoveEntity(messaging: Messaging): MessagingRemoveEntity {
        return when (messaging){
            is Messaging.GroupRemover -> MessagingRemoveEntity(
                operation = messaging.operation,
                notificationKeyName = messaging.keyName,
                notificationKey = messaging.key,
                registrationIds = messaging.tokens)

            else -> throw IllegalStateException()
        }
    }

    override fun asResponseBody(response: Response<ResponseBody>): ResponseBody {
        if (!response.isSuccessful) throw HttpFailureException(
            response.errorBody()?.string() ?: "response unsuccessful")

        val body = response.body() ?: throw HttpFailureException(
            response.message().ifBlank { "response unsuccessful" })

        return body
    }

    override fun asMessagingGetterResponse(response: Response<ResponseBody>): Messaging.Getter.Response {
        val key = gson.fromJson(asResponseBody(response).string(),
            Messaging.Getter.Response::class.java)

        if (key.notification_key.isBlank())
            throw IllegalStateException()

        return key
    }
}