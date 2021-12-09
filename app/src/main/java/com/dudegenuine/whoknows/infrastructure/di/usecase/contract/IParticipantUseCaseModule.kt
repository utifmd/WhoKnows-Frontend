package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.ParticipantRepository
import com.dudegenuine.usecase.participant.*

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantUseCaseModule {
    fun providePostParticipantModule(repos: ParticipantRepository): PostParticipant
    fun provideGetParticipantModule(repos: ParticipantRepository): GetParticipant
    fun providePatchParticipantModule(repos: ParticipantRepository): PatchParticipant
    fun provideDeleteParticipantModule(repos: ParticipantRepository): DeleteParticipant
    fun provideGetParticipantsModule(repos: ParticipantRepository): GetParticipants
}