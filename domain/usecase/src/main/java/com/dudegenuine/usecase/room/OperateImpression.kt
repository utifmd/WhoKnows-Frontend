package com.dudegenuine.usecase.room

import android.util.Log
import com.dudegenuine.model.*
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IImpressionRepository
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.INotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wed, 06 Jul 2022
 * WhoKnows by utifmd
 **/
class OperateImpression
    @Inject constructor(
    private val repoImpression: IImpressionRepository,
    private val repoNotify: INotificationRepository,
    private val repoMessaging: IMessagingRepository){
    operator fun invoke(
        impressed: Boolean, room: Room.Censored, notification: Notification,
        impression: Impression, pusher: Messaging.Pusher): Flow<Resource<String>> = flow {
        try {
            Log.d("invoke", "owner ${room.userId} ${room.isOwner}")
            emit(Resource.Loading())
            if (!room.hasImpressedBefore) {
                repoImpression.create(impression)

                if (!room.isOwner){
                    repoNotify.create(notification)
                    room.user?.tokens?.first()?.let{ repoMessaging.push(pusher.copy(to = it)) }
                    //room.user?.tokens?.forEach{ repoMessaging.push(pusher.copy(to = it)) }
                }
            } else room.impression?.let {
                repoImpression.update(it.impressionId, it.copy(good = impressed)) }
            emit(Resource.Success(impression.impressionId))
        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.THROWABLE_EXCEPTION))
        }
    }
}