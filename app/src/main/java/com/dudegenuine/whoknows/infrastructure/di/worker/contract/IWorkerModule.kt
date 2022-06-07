package com.dudegenuine.whoknows.infrastructure.di.worker.contract

import android.content.Context
import com.dudegenuine.repository.contract.dependency.local.ITokenWorkManager

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
interface IWorkerModule {
    fun provideAlarmRoomWorkerRequest(context: Context): ITokenWorkManager
}