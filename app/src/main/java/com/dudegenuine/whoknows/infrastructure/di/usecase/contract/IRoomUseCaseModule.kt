package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.RoomRepository
import com.dudegenuine.usecase.room.GetRoom
import com.dudegenuine.usecase.user.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomUseCaseModule {
//    fun providePostRoomModule(repos: RoomRepository): PostRoom
    fun provideGetRoomModule(repos: RoomRepository): GetRoom
//    fun providePatchRoomModule(repos: RoomRepository): PatchRoom
//    fun provideDeleteRoomModule(repos: RoomRepository): DeleteRoom
//    fun provideGetRoomsModule(repos: RoomRepository): GetRooms
//    fun provideSignInRoomsModule(repos: RoomRepository): SignInRoom
}