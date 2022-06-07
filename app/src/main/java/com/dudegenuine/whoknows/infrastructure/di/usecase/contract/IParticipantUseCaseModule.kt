package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
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
    val getParticipation: GetParticipation

    val prefs: IPrefsFactory
}