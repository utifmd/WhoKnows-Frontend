package com.dudegenuine.usecase.participation

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
        participant: Participant,
        /*notification: Notification,
        remover: Messaging.GroupRemover,
        pusher: Messaging.Pusher*/): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            reposParticipant.delete(participant.id)
            reposResult.delete(participant.roomId, participant.userId)

            /*reposNotify.create(notification)
            reposMessaging.remove(remover)
            reposMessaging.push(pusher)*/
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