package com.dudegenuine.whoknows.ux.vm.room.contract

import androidx.paging.PagingData
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import com.dudegenuine.whoknows.ux.vm.notification.contract.IMessagingViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class IRoomViewModel: BaseViewModel(),
    IMessagingViewModel, IRoomEventHome, IRoomEventDetail {

    companion object {
        const val KEY_PARTICIPATION_ROOM_ID = "KEY_PARTICIPATION_ROOM_ID"

        const val DEFAULT_BATCH_ROOM = 5
        const val ALREADY_JOINED = "Sorry, you already joined the class."
        const val JUST_KICKED_OUT = "just kicked out by admin from the"
    }

    open fun postRoom(room: Room.Complete){}
    open fun getRoom(id: String){}
    open fun patchRoom(id: String, current: Room.Complete){}
    open fun deleteRoom(id: String){}
    open fun getRooms(page: Int, size: Int){}

    abstract val rooms: Flow<PagingData<Room.Censored>>
    abstract val roomsOwner: Flow<PagingData<Room.Complete>>
    //abstract val roomsOwner:(userId: String) -> Flow<PagingData<Room.Complete>>

    //open fun postBoarding(state: Room.State.BoardingQuiz, onSucceed: (String) -> Unit){}
}