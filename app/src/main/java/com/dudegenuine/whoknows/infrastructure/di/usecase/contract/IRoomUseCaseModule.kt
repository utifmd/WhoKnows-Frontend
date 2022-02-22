package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.room.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomUseCaseModule {

    val postRoom: PostRoom
    val getRoom: GetRoom
    val patchRoom: PatchRoom
    val deleteRoom: DeleteRoom
    val getRooms: GetRooms

    val getBoarding: GetBoarding
    val postBoarding: PostBoarding
    val patchBoarding: PatchBoarding
    val deleteBoarding: DeleteBoarding

    /*val getterOnboard: IRoomRepository.IBoarding.Getter
    val setterOnboard: IRoomRepository.IBoarding.Setter*/

    val currentToken: () -> String
    val currentUserId: () -> String
    val setClipboard: (String, String) -> Unit
}