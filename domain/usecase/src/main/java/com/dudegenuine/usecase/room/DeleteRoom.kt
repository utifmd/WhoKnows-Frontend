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
class DeleteRoom
    @Inject constructor(
    private val repository: IRoomRepository,
    private val reposMessaging: IMessagingRepository) {
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
    operator fun invoke(room: Room.Complete): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val remover = Messaging.GroupRemover(
                key = room.token, keyName = room.id, tokens = listOf(repository.preference.tokenId)
            )
            reposMessaging.remove(remover)
            repository.deleteRemote(room.id)

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
}