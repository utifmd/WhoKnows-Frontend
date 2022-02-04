package com.dudegenuine.whoknows.infrastructure.di.usecase

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

    override val currentUserId: () -> String =
        { repository.currentUserId() },

    override val saveInClipboard: (String, String) -> Unit = { k, v ->
        repository.saveInClipboard(k, v)
    }

): IRoomUseCaseModule