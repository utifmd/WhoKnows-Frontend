package com.dudegenuine.whoknows.infrastructure.di.usecase

import android.content.BroadcastReceiver
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.usecase.room.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class RoomUseCaseModule(
    private val repository: IRoomRepository,

    override val postRoom: PostRoom =
        PostRoom(repository),

    override val getRoom: GetRoom =
        GetRoom(repository),

    override val patchRoom: PatchRoom =
        PatchRoom(repository),

    override val deleteRoom: DeleteRoom =
        DeleteRoom(repository),

    override val getRooms: GetRooms =
        GetRooms(repository),

    override val getBoarding: GetBoarding =
        GetBoarding(repository),

    override val postBoarding: PostBoarding =
        PostBoarding(repository),

    override val patchBoarding: PatchBoarding =
        PatchBoarding(repository),

    override val deleteBoarding: DeleteBoarding =
        DeleteBoarding(repository),

    override val currentToken: () -> String =
        repository.currentToken,

    override val currentUserId: () -> String =
        repository.currentUserId,

    override val currentRunningTime: () -> String =
        repository.currentRunningTime,

    override val setClipboard: (String, String) -> Unit =
        repository.setClipboard,

    override val onTimerThick: ((Double, Boolean) -> Unit) -> BroadcastReceiver =
        repository.onTimerThick

): IRoomUseCaseModule