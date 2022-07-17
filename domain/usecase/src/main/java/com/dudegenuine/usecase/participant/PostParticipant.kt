package com.dudegenuine.usecase.participant

import android.util.Log
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IParticipantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class PostParticipant
    @Inject constructor(
    private val reposParticipant: IParticipantRepository,
    private val reposMessaging: IMessagingRepository) {
    private val TAG: String = javaClass.simpleName
    operator fun invoke(
        current: Participant, roomToken: String): Flow<Resource<Participant>> = flow {
        val adder = Messaging.GroupAdder(current.roomId, listOf(reposParticipant.prefs.tokenId), roomToken)
        val creator = Messaging.GroupCreator(adder.keyName, adder.tokens)
        try {
            emit(Resource.Loading())
            val participant = reposParticipant.create(current)
            val (_, error) = reposMessaging.add(adder)
            error?.let{ message ->
                Log.d(TAG, "invoke: is token blank ${roomToken.isBlank()} | $message")
                if (roomToken.isNotBlank()) reposMessaging.create(creator)
            }
            emit(Resource.Success(participant))
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
    /*operator fun invoke(current: Participant): Flow<Resource<Participant>> = flow {
        try {
            emit(Resource.Loading())
            val participant = reposParticipant.create(current)
            emit(Resource.Success(participant))
        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.THROWABLE_EXCEPTION))
        }
    }*/
}