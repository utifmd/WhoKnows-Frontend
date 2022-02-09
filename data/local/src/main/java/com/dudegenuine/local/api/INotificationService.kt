package com.dudegenuine.local.api

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*
import kotlin.math.roundToInt

/**
 * Wed, 09 Feb 2022
 * WhoKnows by utifmd
 **/
abstract class INotificationService: Service() {

    companion object {
        const val FOREGROUND_TIMER_SERVICE_ID = 1001

        const val TIME_ACTION = "latest_receiver_updated_action"
        const val TIME_KEY = "current_time_key"

        const val CHANNEL_ID = "channel id"
        const val CHANNEL_NAME = "channel name is notification"
    }

    protected val timer = Timer()

    protected abstract val notifyManager: NotificationManager
    protected abstract fun notifyBuilder(): NotificationCompat.Builder

    protected abstract fun taskTimer(): TimerTask

    override fun onBind(intent: Intent?): IBinder?  = null

    fun asString(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onDestroy() {
        timer.cancel()

        super.onDestroy()
    }
}