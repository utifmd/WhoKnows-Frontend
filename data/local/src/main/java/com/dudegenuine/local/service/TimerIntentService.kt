package com.dudegenuine.local.service

import android.content.Intent
import com.dudegenuine.local.manager.contract.IBroadcastReceiverManager.Companion.TIMER_UPDATED
import com.dudegenuine.local.manager.contract.IBroadcastReceiverManager.Companion.TIME_EXTRA
import com.dudegenuine.local.service.contract.ITimerIntentService
import java.util.*

/**
 * Wed, 02 Feb 2022
 * WhoKnows by utifmd
 **/
class TimerIntentService: ITimerIntentService() {
    private inner class Task(var time: Double): TimerTask(){
        val intent = Intent(TIMER_UPDATED)

        override fun run() {
            time++
            intent.putExtra(TIME_EXTRA, time)

            sendBroadcast(intent)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.getDoubleExtra(TIME_EXTRA, 0.0).also { time ->
            timer.scheduleAtFixedRate(Task(time), 0, 1000)
        }

        return START_NOT_STICKY
    }
}