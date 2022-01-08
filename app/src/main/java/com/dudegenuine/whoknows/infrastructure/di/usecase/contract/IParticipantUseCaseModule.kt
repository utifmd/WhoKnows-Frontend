package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.participant.*

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantUseCaseModule {
    val postParticipant: PostParticipant
    val getParticipant: GetParticipant
    val patchParticipant: PatchParticipant
    val deleteParticipant: DeleteParticipant
    val getParticipants: GetParticipants
}