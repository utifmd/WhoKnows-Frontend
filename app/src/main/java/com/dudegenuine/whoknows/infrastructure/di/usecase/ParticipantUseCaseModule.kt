package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IParticipantRepository
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IParticipantUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class ParticipantUseCaseModule(
    private val repository: IParticipantRepository): IParticipantUseCaseModule {

    override val postParticipant: PostParticipant
        get() = PostParticipant(repository)

    override val getParticipant: GetParticipant
        get() = GetParticipant(repository)

    override val patchParticipant: PatchParticipant
        get() = PatchParticipant(repository)

    override val deleteParticipant: DeleteParticipant
        get() = DeleteParticipant(repository)

    override val getParticipants: GetParticipants
        get() = GetParticipants(repository)

}