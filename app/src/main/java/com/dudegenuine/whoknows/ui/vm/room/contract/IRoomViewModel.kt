package com.dudegenuine.whoknows.ui.vm.room.contract

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.vm.notification.contract.IMessagingViewModel
import com.dudegenuine.whoknows.ui.vm.participant.contract.IParticipantViewModel

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomViewModel: IParticipantViewModel, IMessagingViewModel {
    fun postRoom(room: Room)
    fun getRoom(id: String)
    fun patchRoom(id: String, current: Room)
    fun deleteRoom(id: String)
    fun getRooms(page: Int, size: Int)
    fun getRooms(userId: String)

    fun postBoarding(state: Room.RoomState.BoardingQuiz, onSucceed: (String) -> Unit)

    fun getMessagingGroupKey(keyName: String, onSucceed: (String) -> Unit)
    fun createMessagingGroup(messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit)
    /*fun addMessagingGroupMember(
        messaging: Messaging.GroupAdder, onSucceed: () -> Unit)*/
}