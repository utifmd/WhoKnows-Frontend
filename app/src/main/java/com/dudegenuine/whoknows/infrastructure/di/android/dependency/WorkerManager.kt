package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.content.Context
import androidx.work.WorkManager
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
class WorkerManager(
    private val context: Context): IWorkerManager {

    override fun instance(): WorkManager = context.let(WorkManager::getInstance)
}