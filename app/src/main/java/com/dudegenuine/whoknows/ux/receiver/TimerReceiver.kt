package com.dudegenuine.whoknows.ux.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.dudegenuine.repository.contract.dependency.local.ITimerService

/**
 * Sun, 17 Jul 2022
 * WhoKnows by utifmd
 **/
class TimerReceiver
    constructor(): BroadcastReceiver(){
    private val TAG: String = javaClass.simpleName
    private lateinit var forward: (Double, Boolean) -> Unit

    constructor(onForward: (Double, Boolean) -> Unit): this(){
        forward = onForward
    }
    companion object{
        fun instance() = IntentFilter(ITimerService.TIME_ACTION)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceived: trigger")

        val time = intent?.getDoubleExtra(
            ITimerService.INITIAL_TIME_KEY, 0.0) ?: 0.0

        val finished = intent?.getBooleanExtra(
            ITimerService.TIME_UP_KEY, false) ?: false

        forward(time, finished)
    }
}