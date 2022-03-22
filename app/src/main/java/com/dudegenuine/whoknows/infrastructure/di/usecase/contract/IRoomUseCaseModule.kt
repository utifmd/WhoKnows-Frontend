package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import android.content.BroadcastReceiver
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

    val currentToken: () -> String
    val currentUserId: () -> String
    val currentRunningTime: () -> String
    val setClipboard: (String, String) -> Unit
    val onTimerReceived: ((Double, Boolean) -> Unit) -> BroadcastReceiver
}