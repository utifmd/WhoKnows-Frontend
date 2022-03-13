package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.ITimerLauncher
import com.dudegenuine.local.api.ITimerService
import com.dudegenuine.whoknows.ui.service.TimerService

/**
 * Tue, 08 Mar 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class TimerLauncher(
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