package com.dudegenuine.whoknows.ui.vm.room.contract

import androidx.paging.PagingData
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.notification.contract.IMessagingViewModel
import com.dudegenuine.whoknows.ui.vm.participant.contract.IParticipantViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class IRoomViewModel: IParticipantViewModel, IMessagingViewModel, BaseViewModel() {
    companion object {
        const val DEFAULT_BATCH_ROOM = 5
        const val ALREADY_JOINED = "Sorry, you already joined the class."
    }

    abstract fun postRoom(room: Room.Complete)
    abstract fun getRoom(id: String)
    abstract fun patchRoom(id: String, current: Room.Complete)
    abstract fun deleteRoom(id: String)
    abstract fun getRooms(page: Int, size: Int)

    abstract val rooms: Flow<PagingData<Room.Censored>>
    abstract val roomsOwner: Flow<PagingData<Room.Complete>>

    abstract fun postBoarding(state: Room.State.BoardingQuiz, onSucceed: (String) -> Unit)
}