package com.dudegenuine.whoknows.ux.vm.participation.contract

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Participation

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantViewModel: IParticipationEvent {
    companion object {
        const val BATCH_SIZE = 5
    }

    //fun initParticipant(participant: Participant){}
    fun postParticipant(participant: Participant){}
    fun getParticipant(id: String){}
    fun patchParticipant(id: String, current: Participant){}
    fun deleteParticipant(id: String){}
    fun getParticipants(page: Int, size: Int){}

    fun getBoarding(onSucceed: (Participation) -> Unit){}
    fun deleteBoarding(onSucceed: (String) -> Unit){}
    //fun postBoarding(state: Participation, onSucceed: (Participation) -> Unit){}
    fun postBoarding(state: Participation){}
    fun patchBoarding(state: Participation){}
}