package com.dudegenuine.usecase.participation

import com.dudegenuine.model.*
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Sat, 25 Jun 2022
 * WhoKnows by utifmd
 **/
class PostParticipation
    @Inject constructor(
    private val reposParticipant: IParticipantRepository,
    private val reposRoom: IRoomRepository,
    private val reposResult: IResultRepository,
    private val reposMessaging: IMessagingRepository,
    private val reposNotify: INotificationRepository) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        participant: Participant,
        result: Result,
        notification: Notification,
        addMessaging: Messaging.GroupAdder,
        pushMessaging: Messaging.Pusher): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            reposRoom.timer.stop()
            reposMessaging.add(addMessaging)
            reposParticipant.update(participant.id, participant.copy(expired = true))
            reposResult.create(result)
            reposNotify.create(notification)
            reposRoom.deleteBoardingLocal()
            reposMessaging.push(pushMessaging)
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