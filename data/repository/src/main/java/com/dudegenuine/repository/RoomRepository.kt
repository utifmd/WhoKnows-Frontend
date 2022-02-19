package com.dudegenuine.repository

import com.dudegenuine.local.api.IClipboardManager
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.local.api.IPreferenceManager.Companion.ONBOARD_PARTICIPANT_ID
import com.dudegenuine.local.api.IPreferenceManager.Companion.ONBOARD_ROOM_ID
import com.dudegenuine.local.service.contract.ICurrentBoardingDao
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IRoomDataMapper
import com.dudegenuine.remote.service.contract.IRoomService
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.IRoomRepository.Companion.CURRENT_PARTICIPANT_ID
import com.dudegenuine.repository.contract.IRoomRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomRepository
    @Inject constructor(
    private val service: IRoomService,
    private val local: ICurrentBoardingDao,
    private val mapper: IRoomDataMapper,
    private val prefs: IPreferenceManager,
    private val clip: IClipboardManager): IRoomRepository {
    private val TAG: String = javaClass.simpleName

    override val setClipboard: (String, String) -> Unit = clip::applyPlainText
    override val currentToken: () -> String = { prefs.read(IMessagingRepository.MESSAGING_TOKEN) }
    override val currentUserId: () -> String = { prefs.read(CURRENT_USER_ID) }
    override val currentParticipant: () -> String = { prefs.read(CURRENT_PARTICIPANT_ID) }

    override suspend fun create(room: Room): Room = /*try {*/ mapper.asRoom(
        service.create(mapper.asEntity(room)))
    /*} catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }*/

    override suspend fun read(id: String): Room = /*try {*/ mapper.asRoom(
        service.read(id))

    /*} catch (e: Exception){
        Log.d(TAG, e.message ?: "throwable")
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }*/

    override suspend fun update(id: String, room: Room): Room = /*try {*/ mapper.asRoom(
        service.update(id, mapper.asEntity(room)))
    /*} catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }*/

    override suspend fun delete(id: String) = /*try {*/
        service.delete(id)
    /*} catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }*/

    override suspend fun list(page: Int, size: Int): List<Room> = /*try {*/ mapper.asRooms(
        service.list(page, size))
    /*} catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }*/

    override suspend fun list(userId: String): List<Room> = /*try {*/ mapper.asRooms(
        service.list(userId))

    override suspend fun load(participantId: String?): Room.RoomState.BoardingQuiz {
        val model = participantId ?: currentParticipant()
        val currentBoarding = local.read(model) ?: throw HttpFailureException(NOT_FOUND)

        return mapper.asRoomBoardingQuiz(currentBoarding)
    }

    override suspend fun save(boarding: Room.RoomState.BoardingQuiz) {
        local.create(mapper.asCurrentBoarding(boarding)).also {
            prefs.write(CURRENT_PARTICIPANT_ID, boarding.participantId)
        }
    }

    override suspend fun replace(boarding: Room.RoomState.BoardingQuiz) {
        local.update(mapper.asCurrentBoarding(boarding))
    }

    override suspend fun unload(participantId: String) {
        val store = local.read(participantId)

        store?.let {
            prefs.write(CURRENT_PARTICIPANT_ID, "")

            local::delete
        }
    }

    override val getterOnboard = object : IRoomRepository.IBoarding.Getter {
        override val roomId: () -> String = { prefs.read(ONBOARD_ROOM_ID) }

        override val participantId: () -> String = { prefs.read(ONBOARD_PARTICIPANT_ID) }
    }

    override val setterOnboard = object : IRoomRepository.IBoarding.Setter {
        override fun roomId(id: String) = prefs.write(ONBOARD_ROOM_ID, id)

        override fun participantId(id: String) = prefs.write(ONBOARD_PARTICIPANT_ID, id)
    }
}