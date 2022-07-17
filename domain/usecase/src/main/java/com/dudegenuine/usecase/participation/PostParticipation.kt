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
@OptIn(ExperimentalCoroutinesApi::class)
class PostParticipation
    @Inject constructor(
    private val reposParticipant: IParticipantRepository,
    private val reposRoom: IRoomRepository,
    private val reposResult: IResultRepository,
    private val reposMessaging: IMessagingRepository,
    private val reposNotify: INotificationRepository) {
    private val storedParticipantId
        get() = reposParticipant.prefs.participationParticipantId

    operator fun invoke(
        participant: Participant, result: Result, notification: Notification): Flow<Resource<String>> = flow {
        val messaging = Messaging.Pusher(notification.title, notification.event, notification.imageUrl, to = notification.to)
        try {
            emit(Resource.Loading())
            reposParticipant.update(storedParticipantId, participant.copy(expired = true))
            reposResult.create(result)
            reposNotify.create(notification)
            reposMessaging.push(messaging)
            reposRoom.deleteBoardingLocal()
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