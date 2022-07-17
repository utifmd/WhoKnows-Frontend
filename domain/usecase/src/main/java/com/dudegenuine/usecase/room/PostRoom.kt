package com.dudegenuine.usecase.room

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.Utility.EMPTY_STRING
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class PostRoom
    @Inject constructor(
    private val repository: IRoomRepository,
    private val reposMessaging: IMessagingRepository) {

    operator fun invoke(current: Room.Complete): Flow<Resource<Room.Complete>> = flow {
        try {
            emit(Resource.Loading())
            val creator = Messaging.GroupCreator(current.id, listOf(repository.preference.tokenId))
            val (notificationKey, error) = reposMessaging.create(creator)
            val room = repository.createRemote(current.copy(token = error?.let{ EMPTY_STRING } ?: notificationKey))
            emit(Resource.Success(room))
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