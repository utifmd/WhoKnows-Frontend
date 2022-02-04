package com.dudegenuine.local.service.contract

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

/**
 * Wed, 02 Feb 2022
 * WhoKnows by utifmd
 **/
abstract class ITimerIntentService: Service() {
    protected val timer = Timer()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}