package com.dudegenuine.local.api

import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.dudegenuine.local.api.contract.IServiceCoroutine
import java.util.*
import kotlin.math.roundToInt

/**
 * Wed, 09 Feb 2022
 * WhoKnows by utifmd
 **/
abstract class ITimerService: IServiceCoroutine() {

    companion object {
        const val FOREGROUND_TIMER_SERVICE_ID = 1001

        const val TIME_ACTION = "latest_receiver_updated_action"
        const val INITIAL_TIME_KEY = "exact_time_key"
        const val TIME_UP_KEY = "finish_time_key"

        const val CHANNEL_ID = "channel id"
        const val CHANNEL_NAME = "channel name is notification"

        fun asString(time: Double): String {
            val resultInt = time.roundToInt()
            val hours = resultInt % 86400 / 3600
            val minutes = resultInt % 86400 % 3600 / 60
            val seconds = resultInt % 86400 % 3600 % 60

            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    protected val timer = Timer()

    // protected abstract val notifyManager: NotificationManager
    /*protected abstract fun notifyBuilder(): NotificationCompat.Builder*/

    protected abstract val taskTimerListener: (Context) -> TimerTask
    // protected abstract val taskTimer: TimerTask
    /*protected abstract fun taskTimer(): TimerTask*/

    override fun onBind(intent: Intent?): IBinder?  = null

    override fun onDestroy() {
        job.cancel()
        timer.cancel()

        super.onDestroy()
    }
}