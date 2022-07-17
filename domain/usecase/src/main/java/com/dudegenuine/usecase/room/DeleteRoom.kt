package com.dudegenuine.usecase.room

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class DeleteRoom
    @Inject constructor(
    private val repository: IRoomRepository,
    private val reposResult: IResultRepository,
    private val reposNotifier: INotificationRepository,
    private val reposFile: IFileRepository,
    private val reposMessaging: IMessagingRepository) {
    operator fun invoke(
        room: Room.Complete, notification: Notification): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val remover = Messaging.GroupRemover(room.id, listOf(repository.preference.tokenId), room.token)
            val pusher = Messaging.Pusher(
                notification.title, notification.event, notification.imageUrl,
                listOf(room.userId, room.id).joinToString("|"), notification.to)

            val fileUrls = room.questions.map{ it.images }.flatten()

            fileUrls.map{ it.substringAfterLast("/") }.forEach{ fileId ->
                reposFile.delete(fileId)
            }
            room.participants.filter{ it.expired }.forEach{ participant ->
                reposResult.delete(participant.roomId, participant.userId)
                reposNotifier.create(notification.copy(recipientId = participant.userId))
            }
            repository.deleteRemote(room.id)
            if (room.token.isNotBlank()) {
                reposMessaging.push(pusher)
                reposMessaging.remove(remover)
            }
            emit(Resource.Success(room.id))
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
    operator fun invoke(id: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            repository.deleteRemote(id)
            emit(Resource.Success(id))
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