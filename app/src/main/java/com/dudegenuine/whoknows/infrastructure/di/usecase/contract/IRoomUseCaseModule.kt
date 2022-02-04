package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.room.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomUseCaseModule {
    val currentUserId: () -> String
    val saveInClipboard: (String, String) -> Unit

    val postRoom: PostRoom
    val getRoom: GetRoom
    val patchRoom: PatchRoom
    val deleteRoom: DeleteRoom
    val getRooms: GetRooms
}