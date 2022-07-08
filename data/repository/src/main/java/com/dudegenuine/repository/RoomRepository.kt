package com.dudegenuine.repository

import android.util.Log
import androidx.paging.PagingSource
import com.dudegenuine.local.entity.RoomCensoredTable
import com.dudegenuine.local.entity.RoomCompleteTable
import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.model.Participation
import com.dudegenuine.model.ResourcePaging
import com.dudegenuine.model.Room
import com.dudegenuine.model.Search
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IRoomDataMapper
import com.dudegenuine.remote.service.contract.IRoomService
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.IRoomRepository.Companion.NOT_FOUND
import com.dudegenuine.repository.contract.dependency.local.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomRepository
    @Inject constructor(
    private val service: IRoomService,
    val mapper: IRoomDataMapper,
    private val local: IWhoKnowsDatabase,

    override val workManager: IWorkerManager,
    override val workRequest: ITokenWorkManager,
    override val alarmManager: IAlarmManager,
    override val receiver: IReceiverFactory,
    override val preference: IPrefsFactory,
    override val clipboard: IClipboardManager,
    override val timer: ITimerLauncher,
    override val share: IShareLauncher): IRoomRepository {
    private val TAG: String = javaClass.simpleName
    private val daoBoarding get() = local.daoBoarding()

    override suspend fun createRemote(room: Room.Complete): Room.Complete = mapper.asRoom(
        service.create(mapper.asEntity(room)))

    override suspend fun readRemote(id: String): Room.Complete = mapper.asRoom(
        service.read(id))

    override suspend fun updateRemote(id: String, room: Room.Complete): Room.Complete = mapper.asRoom(
        service.update(id, mapper.asEntity(room)))

    override suspend fun deleteRemote(id: String) =
        service.delete(id)

    override suspend fun listCompleteRemote(page: Int, size: Int): List<Room.Complete> = mapper.asRooms(
        service.listComplete(page, size))

    override suspend fun listCensoredRemote(page: Int, size: Int): List<Room.Censored> = mapper.asRoomsCensored(
        service.listCensored(page, size))

    override suspend fun listCompleteRemote(userId: String, page: Int, size: Int): List<Room.Complete> = mapper.asRooms(
        service.listComplete(userId, page, size))

    override fun pageCensoredRemote(batchSize: Int): PagingSource<Int, Room.Censored> =
        mapper.asPagingCensoredSource { page -> listCensoredRemote(page, batchSize) }

    override fun pageCompleteRemote(userId: String, batchSize: Int): PagingSource<Int, Room.Complete> =
        mapper.asPagingCompleteSource { page ->
            listCompleteRemote(userId, page, batchSize)
        }
    override fun remoteSearchPageCensored(
        query: String, batch: Int): PagingSource<Int, Room.Censored> = try {
        ResourcePaging{ page -> mapper.asRoomsCensored(
            service.listCensoredSearched(query, page, batch)) }
    } catch (e: Exception){ ResourcePaging{ emptyList() }}

    override fun remoteSearchSource(query: String, batch: Int): PagingSource<Int, Search<*>> = try {
        ResourcePaging{ page -> mapper.asRoomsCensored(
            service.listCensoredSearched(query, page, batch)).map{ Search.Room(it) } }
    } catch (e: Exception){ ResourcePaging{ emptyList() }}

    override suspend fun clearParticipation(): Flow<Unit> = flowOf(deleteBoardingLocal())

    override suspend fun createBoardingLocal(participation: Participation) {
        daoBoarding.create(mapper.asParticipationTable(participation)).also {
            onParticipantRoomIdChange(participation.roomId)
            onParticipantTimeLeftChange(participation.roomMinute)
        }
    }
    override suspend fun readBoardingLocal(): Participation { //val model = preference.participationId
        val currentBoarding = local.daoBoarding().read() ?: throw HttpFailureException(NOT_FOUND)

        return mapper.asParticipation(currentBoarding)
    }
    override suspend fun updateBoardingLocal(participation: Participation) {
        daoBoarding.update(mapper.asParticipationTable(participation))
    }
    override suspend fun deleteBoardingLocal() {
        //val store = daoBoarding.read(/*preference.participationId*/)

        daoBoarding.delete()
        onParticipantRoomIdChange("")
        onParticipantTimeLeftChange(0)

        /*if (store != null) {
            daoBoarding.delete(store)
            return
        }
        daoBoarding.deleteAll()*/ //onParticipationIdChange("")
    }
    override suspend fun listCensoredLocal(): List<RoomCensoredTable> = emptyList() //local.roomCensoredDao().list()
    override suspend fun listCompleteLocal(userId: String): List<RoomCompleteTable> = emptyList()//local.roomCompleteDao().list(userId)

    private fun onParticipantTimeLeftChange(time: Int){
        Log.d(TAG, "onParticipantTimeLeftChange: $time")
        preference.participationTimeLeft = time
    }
    private fun onParticipantRoomIdChange(roomId: String){
        Log.d(TAG, "onParticipantRoomIdChange: $roomId")
        preference.participationRoomId = roomId
    }
}