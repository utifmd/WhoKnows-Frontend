package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.RoomRepository
import com.dudegenuine.usecase.room.GetRoom
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object RoomUseCaseModule: IRoomUseCaseModule {

    @Provides
    @ViewModelScoped
    override fun provideGetRoomModule(repos: RoomRepository): GetRoom {
        return GetRoom(repos)
    }
}