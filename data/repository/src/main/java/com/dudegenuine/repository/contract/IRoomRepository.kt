package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.local.entity.RoomCensoredTable
import com.dudegenuine.local.entity.RoomCompleteTable
import com.dudegenuine.model.Participation
import com.dudegenuine.model.Room
import com.dudegenuine.model.Search
import com.dudegenuine.repository.contract.dependency.local.*
import kotlinx.coroutines.flow.Flow


/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomRepository {
    companion object {
        const val NOT_FOUND = "Room boarding not found."
    }

    suspend fun createRemote(room: Room.Complete): Room.Complete
    suspend fun readRemote(id: String): Room.Complete
    suspend fun updateRemote(id: String, room: Room.Complete): Room.Complete
    suspend fun deleteRemote(id: String)
    suspend fun listCompleteRemote(page: Int, size: Int): List<Room.Complete>
    suspend fun listCensoredRemote(page: Int, size: Int): List<Room.Censored>
    suspend fun listCompleteRemote(userId: String, page: Int, size: Int): List<Room.Complete>
    fun pageCensoredRemote(batchSize: Int): PagingSource<Int, Room.Censored>
    fun pageCompleteRemote(userId: String, batchSize: Int): PagingSource<Int, Room.Complete>
    fun remoteSearchPageCensored(query: String, batch: Int): PagingSource<Int, Room.Censored>
    fun remoteSearchSource(query: String, batch: Int): PagingSource<Int, Search<*>>

    suspend fun clearParticipation(): Flow<Unit>

    suspend fun listCensoredLocal(): List<RoomCensoredTable>
    suspend fun listCompleteLocal(userId: String): List<RoomCompleteTable>

    suspend fun readBoardingLocal(): Participation
    suspend fun createBoardingLocal(participation: Participation)
    suspend fun updateBoardingLocal(participation: Participation)
    suspend fun deleteBoardingLocal()

    val workManager: IWorkerManager
    val workRequest: ITokenWorkManager
    val alarmManager: IAlarmManager
    val receiver: IReceiverFactory
    val preference: IPrefsFactory
    val clipboard: IClipboardManager
    val timer: ITimerLauncher
    val share: IShareLauncher
}