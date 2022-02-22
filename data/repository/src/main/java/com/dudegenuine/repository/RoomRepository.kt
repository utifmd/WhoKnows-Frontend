package com.dudegenuine.repository

import com.dudegenuine.local.api.IClipboardManager
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.local.api.ITimerService
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

    override val currentToken: () -> String =
        { prefs.read(IMessagingRepository.MESSAGING_TOKEN) }

    override val currentUserId: () -> String =
        { prefs.read(CURRENT_USER_ID) }

    override val currentRunningTime: () -> String =
        { prefs.read(ITimerService.INITIAL_TIME_KEY) }

    override val currentParticipant: () -> String =
        { prefs.read(CURRENT_PARTICIPANT_ID) }

    override suspend fun create(room: Room): Room = mapper.asRoom(
        service.create(mapper.asEntity(room)))

    override suspend fun read(id: String): Room = mapper.asRoom(
        service.read(id))

    override suspend fun update(id: String, room: Room): Room = mapper.asRoom(
        service.update(id, mapper.asEntity(room)))

    override suspend fun delete(id: String) =
        service.delete(id)

    override suspend fun list(page: Int, size: Int): List<Room> = mapper.asRooms(
        service.list(page, size))

    override suspend fun list(userId: String): List<Room> = mapper.asRooms(
        service.list(userId))

    override suspend fun load(participantId: String?): Room.RoomState.BoardingQuiz {
        val model = participantId ?: currentParticipant()
        val currentBoarding = local.read(model) ?: throw HttpFailureException(NOT_FOUND)

        return mapper.asBoardingQuiz(currentBoarding)
    }

    override suspend fun save(boarding: Room.RoomState.BoardingQuiz) {
        local.create(mapper.asBoardingQuizTable(boarding)).also {
            prefs.write(CURRENT_PARTICIPANT_ID, boarding.participantId)
        }
    }

    override suspend fun replace(boarding: Room.RoomState.BoardingQuiz) {
        local.update(mapper.asBoardingQuizTable(boarding))
    }

    override suspend fun unload() {
        val store = local.read(currentParticipant())

        if (store != null) local.delete(store)
        else local.delete()

        prefs.write(CURRENT_PARTICIPANT_ID, "")
    }
}