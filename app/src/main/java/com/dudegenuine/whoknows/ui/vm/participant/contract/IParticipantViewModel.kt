package com.dudegenuine.whoknows.ui.vm.participant.contract

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Room

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantViewModel {
    fun initParticipant(participant: Participant){}
    fun postParticipant(participant: Participant){}
    fun getParticipant(id: String){}
    fun patchParticipant(id: String, current: Participant){}
    fun deleteParticipant(id: String){}
    fun getParticipants(page: Int, size: Int){}

    fun getBoarding(onSucceed: (Room.RoomState.BoardingQuiz) -> Unit){}
    fun deleteBoarding(onSucceed: (String) -> Unit){}
    fun postBoarding(state: Room.RoomState.BoardingQuiz){}
    fun patchBoarding(state: Room.RoomState.BoardingQuiz){}
}