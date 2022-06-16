package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.usecase.participation.DeleteBoarding
import com.dudegenuine.usecase.participation.GetBoarding
import com.dudegenuine.usecase.participation.PatchBoarding
import com.dudegenuine.usecase.participation.PostBoarding
import com.dudegenuine.usecase.room.*
import com.dudegenuine.usecase.search.SearchRooms
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class RoomUseCaseModule(
    private val repository: IRoomRepository,

    override val postRoom:
        PostRoom = PostRoom(repository),

    override val getRoom:
        GetRoom = GetRoom(repository),

    override val patchRoom:
        PatchRoom = PatchRoom(repository),

    override val deleteRoom:
        DeleteRoom = DeleteRoom(repository),

    override val getRooms:
        GetRooms = GetRooms(repository),

    override val getBoarding:
    GetBoarding = GetBoarding(repository),

    override val postBoarding:
    PostBoarding = PostBoarding(repository),

    override val patchBoarding:
    PatchBoarding = PatchBoarding(repository),

    override val deleteBoarding:
    DeleteBoarding = DeleteBoarding(repository),

    override val searchRooms:
    SearchRooms = SearchRooms(repository),

    override val workManager: IWorkerManager = repository.workManager,
    override val workRequest: ITokenWorkManager = repository.workRequest,
    override val alarmManager: IAlarmManager = repository.alarmManager,
    override val receiver: IReceiverFactory = repository.receiver,
    override val preferences: IPrefsFactory = repository.preference,
    override val clipboard: IClipboardManager = repository.clipboard,
    override val timer: ITimerLauncher = repository.timer,
    override val share: IShareLauncher = repository.share

): IRoomUseCaseModule