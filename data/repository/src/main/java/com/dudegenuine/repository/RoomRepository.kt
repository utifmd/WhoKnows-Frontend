package com.dudegenuine.repository

import android.content.BroadcastReceiver
import androidx.paging.PagingSource
import com.dudegenuine.local.api.IClipboardManager
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.local.api.IReceiverFactory
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
    private val receiver: IReceiverFactory,
    private val local: ICurrentBoardingDao,
    private val mapper: IRoomDataMapper,
    private val prefs: IPreferenceManager,
    private val clip: IClipboardManager): IRoomRepository {
    private val TAG: String = javaClass.simpleName

    override val timerReceived: ((Double, Boolean) -> Unit) ->
        BroadcastReceiver = receiver.timerReceived

    override val setClipboard: (String, String) -> Unit = clip::applyPlainText

    override val currentToken: () -> String =
        { prefs.readString(IMessagingRepository.MESSAGING_TOKEN) }

    override val currentUserId: () -> String =
        { prefs.readString(CURRENT_USER_ID) }

    override val currentRunningTime: () -> String =
        { prefs.readString(ITimerService.INITIAL_TIME_KEY) }

    override val currentParticipant: () -> String =
        { prefs.readString(CURRENT_PARTICIPANT_ID) }

    override suspend fun create(room: Room.Complete): Room.Complete = mapper.asRoom(
        service.create(mapper.asEntity(room)))

    override suspend fun read(id: String): Room.Complete = mapper.asRoom(
        service.read(id))

    override suspend fun update(id: String, room: Room.Complete): Room.Complete = mapper.asRoom(
        service.update(id, mapper.asEntity(room)))

    override suspend fun delete(id: String) =
        service.delete(id)

    override suspend fun listComplete(page: Int, size: Int): List<Room.Complete> {
        return mapper.asRooms(service.listComplete(page, size))
    }

    override suspend fun listCensored(page: Int, size: Int): List<Room.Censored> =
        mapper.asRoomsCensored(service.listCensored(page, size))

    override suspend fun listComplete(userId: String, page: Int, size: Int): List<Room.Complete> = mapper.asRooms(
        service.listComplete(userId, page, size))

    override fun page(batchSize: Int): PagingSource<Int, Room.Censored> =
        mapper.asPagingCensoredSource { page ->
            listCensored(page, batchSize)
        }

    override fun page(userId: String, batchSize: Int): PagingSource<Int, Room.Complete> =
        mapper.asPagingCompleteSource { page ->
            listComplete(userId, page, batchSize)
        }

    override suspend fun load(participantId: String?): Room.State.BoardingQuiz {
        val model = participantId ?: currentParticipant()
        val currentBoarding = local.read(model) ?: throw HttpFailureException(NOT_FOUND)

        return mapper.asBoardingQuiz(currentBoarding)
    }

    override suspend fun save(boarding: Room.State.BoardingQuiz) {
        local.create(mapper.asBoardingQuizTable(boarding)).also {
            prefs.write(CURRENT_PARTICIPANT_ID, boarding.participantId)
        }
    }

    override suspend fun replace(boarding: Room.State.BoardingQuiz) {
        local.update(mapper.asBoardingQuizTable(boarding))
    }

    override suspend fun unload() {
        val store = local.read(currentParticipant())

        if (store != null) local.delete(store)
        else local.delete()

        prefs.write(CURRENT_PARTICIPANT_ID, "")
    }
}