package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.usecase.participation.DeleteParticipation
import com.dudegenuine.usecase.participation.PostParticipation

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
    val postParticipation: PostParticipation
    val deleteParticipation: DeleteParticipation

    val prefs: IPrefsFactory
}