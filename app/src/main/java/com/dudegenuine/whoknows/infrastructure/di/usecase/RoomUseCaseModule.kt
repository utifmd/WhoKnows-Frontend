package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.RoomRepository
import com.dudegenuine.usecase.room.*
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
    override fun providePostRoomModule(repos: RoomRepository): PostRoom {
        return PostRoom(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetRoomModule(repos: RoomRepository): GetRoom {
        return GetRoom(repos)
    }

    @Provides
    @ViewModelScoped
    override fun providePatchRoomModule(repos: RoomRepository): PatchRoom {
        return PatchRoom(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideDeleteRoomModule(repos: RoomRepository): DeleteRoom {
        return DeleteRoom(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetRoomsModule(repos: RoomRepository): GetRooms {
        return GetRooms(repos)
    }
}