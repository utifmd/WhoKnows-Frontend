package com.dudegenuine.usecase.room

import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class GetRoom
    @Inject constructor(
    private val reposRoom: IRoomRepository,
    private val reposUser: IUserRepository) {
    private val TAG: String = javaClass.simpleName
    private val currentUserId get() = reposRoom.preference.userId
    private val participationRoomId get() = reposRoom.preference.participationRoomId

    operator fun invoke(id: String): Flow<Resource<Room.Complete>> = flow {
        try {
            emit(Resource.Loading())

            val user = reposUser.localRead(currentUserId)
            val isUserIsFree = user.participants.all { it.expired }

            val room = reposRoom.readRemote(id)
            val isUserOffBoarding = participationRoomId.isBlank()
            val isRoomMeetNewUser = room.participants.all{ currentUserId != it.userId }
            val isRoomMeetOldUser = room.participants.any{ currentUserId == it.userId && !it.expired }
            val isRoomMeetExactUser = room.participants.any{ currentUserId == it.userId && it.expired }
            room.apply {
                isOwner = currentUserId == room.userId
                isJoinAccepted = isUserOffBoarding && isUserIsFree && isRoomMeetNewUser
                isParticipated = isRoomMeetOldUser
                isParticipant = isRoomMeetExactUser
                impressionSize = room.impressions.count{ it.good }
                hasImpressedBefore = room.impressions.any { it.userId == currentUserId }
                impressed = room.impressions.any { it.userId == currentUserId && it.good }
            }
            /*Log.d(TAG, "onDetailingRoom: isUserOffBoarding = $isUserOffBoarding") // notification screen by room participated
            Log.d(TAG, "onDetailingRoom: isUserIsFree = $isUserIsFree") // delete room directly complete
            Log.d(TAG, "onDetailingRoom: isRoomMeetNewUser = $isRoomMeetNewUser")
            Log.d(TAG, "onDetailingRoom: isRoomMeetOldUser = $isRoomMeetOldUser")
            Log.d(TAG, "onDetailingRoom: isRoomMeetExactUser = $isRoomMeetExactUser")*/
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
    /*operator fun invoke(id: String): Flow<Resource<Room.Complete>> = flow {
        try {
            emit(Resource.Loading())
            val room = repository.readRemote(id)
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
    }*/
}