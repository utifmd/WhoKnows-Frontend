package com.dudegenuine.whoknows.ui.vm.participant.contract

import com.dudegenuine.model.Participant

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantViewModel {
    fun initParticipant(participant: Participant)
    fun postParticipant(participant: Participant)
    fun getParticipant(id: String)
    fun patchParticipant(id: String, current: Participant)
    fun deleteParticipant(id: String)
    fun getParticipants(page: Int, size: Int)
}