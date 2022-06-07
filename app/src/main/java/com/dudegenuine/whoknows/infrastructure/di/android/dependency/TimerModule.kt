package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.content.Context
import android.content.Intent
import com.dudegenuine.repository.contract.dependency.local.ITimerLauncher
import com.dudegenuine.repository.contract.dependency.local.ITimerService
import com.dudegenuine.whoknows.ux.service.TimerService

/**
 * Tue, 08 Mar 2022
 * WhoKnows by utifmd
 **/
class TimerModule(
    private val context: Context): ITimerLauncher {
    val service = Intent(context, TimerService::class.java)

    override fun start(time: Double): Intent {
        return service.putExtra(ITimerService.INITIAL_TIME_KEY, time)
            .apply(context::startService)
    }

    override fun stop() {
        context.stopService(service)
    }
}