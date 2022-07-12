package com.dudegenuine.usecase.room

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
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
class PatchRoom
    @Inject constructor(
    private val reposRoom: IRoomRepository,
    private val reposMessaging: IMessagingRepository) {

    operator fun invoke(id: String, current: Room.Complete): Flow<Resource<Room.Complete>> = flow {
        try {
            emit(Resource.Loading())
            val room = reposRoom.updateRemote(id, current)
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
    private val tokenId get() = reposRoom.preference.tokenId
    operator fun invoke(selected: Boolean, current: Room.Complete): Flow<Resource<Room.Complete>> = flow {
        try { // TODO: please chain the requests with the flow coroutine
            emit(Resource.Loading())
            val room = reposRoom.updateRemote(current.id, current)
            if (selected)
                reposMessaging.create(Messaging.GroupCreator(current.id, listOf(tokenId)))
            else reposMessaging.remove(Messaging.GroupRemover(current.id, listOf(tokenId), current.token))
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