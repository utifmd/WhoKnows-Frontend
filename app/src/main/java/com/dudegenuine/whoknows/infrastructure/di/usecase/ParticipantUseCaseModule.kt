package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.ParticipantRepository
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IParticipantUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object ParticipantUseCaseModule: IParticipantUseCaseModule {

    @Provides
    @ViewModelScoped
    override fun providePostParticipantModule(repos: ParticipantRepository): PostParticipant {
        return PostParticipant(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetParticipantModule(repos: ParticipantRepository): GetParticipant {
        return GetParticipant(repos)
    }

    @Provides
    @ViewModelScoped
    override fun providePatchParticipantModule(repos: ParticipantRepository): PatchParticipant {
        return PatchParticipant(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideDeleteParticipantModule(repos: ParticipantRepository): DeleteParticipant {
        return DeleteParticipant(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetParticipantsModule(repos: ParticipantRepository): GetParticipants {
        return GetParticipants(repos)
    }
}