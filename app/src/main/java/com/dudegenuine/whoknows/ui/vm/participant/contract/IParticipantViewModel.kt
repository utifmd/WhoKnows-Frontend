package com.dudegenuine.whoknows.ui.vm.participant.contract

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Room

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantViewModel {
    companion object {
        const val BATCH_SIZE = 5
    }

    fun initParticipant(participant: Participant){}
    fun postParticipant(participant: Participant){}
    fun getParticipant(id: String){}
    fun patchParticipant(id: String, current: Participant){}
    fun deleteParticipant(id: String){}
    fun getParticipants(page: Int, size: Int){}

    fun getBoarding(onSucceed: (Room.State.BoardingQuiz) -> Unit){}
    fun deleteBoarding(onSucceed: (String) -> Unit){}
    fun postBoarding(state: Room.State.BoardingQuiz){}
    fun patchBoarding(state: Room.State.BoardingQuiz){}
}