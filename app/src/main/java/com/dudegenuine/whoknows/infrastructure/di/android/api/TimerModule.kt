package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.content.Context
import android.content.Intent
import com.dudegenuine.local.api.ITimerLauncher
import com.dudegenuine.local.api.ITimerService
import com.dudegenuine.whoknows.ui.service.TimerService

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