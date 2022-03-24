package com.dudegenuine.whoknows.ui.vm.room.contract

import androidx.paging.PagingData
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.vm.notification.contract.IMessagingViewModel
import com.dudegenuine.whoknows.ui.vm.participant.contract.IParticipantViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomViewModel: IParticipantViewModel, IMessagingViewModel {
    companion object {
        const val DEFAULT_BATCH_ROOM = 5
        const val ALREADY_JOINED = "Sorry, you already joined the class."
    }

    fun postRoom(room: Room.Complete)
    fun getRoom(id: String)
    fun patchRoom(id: String, current: Room.Complete)
    fun deleteRoom(id: String)
    fun getRooms(page: Int, size: Int)

    val rooms: Flow<PagingData<Room.Complete>>
    val roomsOwner: Flow<PagingData<Room.Complete>>

    fun postBoarding(state: Room.State.BoardingQuiz, onSucceed: (String) -> Unit)
}