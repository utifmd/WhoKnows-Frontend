package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.usecase.participation.DeleteBoarding
import com.dudegenuine.usecase.participation.GetBoarding
import com.dudegenuine.usecase.participation.PatchBoarding
import com.dudegenuine.usecase.participation.PostBoarding
import com.dudegenuine.usecase.room.*
import com.dudegenuine.usecase.search.SearchRooms

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
    val searchRooms: SearchRooms

    val workManager: IWorkerManager
    val workRequest: ITokenWorkManager
    val alarmManager: IAlarmManager
    val receiver: IReceiverFactory
    val preferences: IPrefsFactory
    val clipboard: IClipboardManager
    val timer: ITimerLauncher
    val share: IShareLauncher
}