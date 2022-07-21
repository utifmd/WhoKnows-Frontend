package com.dudegenuine.usecase.participation

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.IParticipantRepository
import com.dudegenuine.repository.contract.IResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Sun, 26 Jun 2022
 * WhoKnows by utifmd
 **/
class DeleteParticipation
    @Inject constructor(
    private val reposParticipant: IParticipantRepository,
    private val reposResult: IResultRepository,
    private val reposMessaging: IMessagingRepository,
    private val reposNotify: INotificationRepository){

    operator fun invoke(
        participant: Participant, notification: Notification): Flow<Resource<String>> = flow {
        val remover = Messaging.GroupRemover(notification.roomId, listOf(reposParticipant.prefs.tokenId), notification.to)
        val pusher = Messaging.Pusher(notification.title, notification.event, notification.imageUrl, participant.userId.plus("|"), notification.to)
        try {
            emit(Resource.Loading())
            reposParticipant.delete(participant.id)
            if (participant.expired) {
                reposResult.delete(participant.roomId, participant.userId)
                reposNotify.create(notification)
                if (pusher.to.isNotBlank()) {
                    reposMessaging.push(pusher)
                    reposMessaging.remove(remover)
                }
            }
            emit(Resource.Success(participant.id))
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
    operator fun invoke(participant: Participant): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            reposParticipant.delete(participant.id)
            reposResult.delete(participant.roomId, participant.userId)
            emit(Resource.Success(participant.id))
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