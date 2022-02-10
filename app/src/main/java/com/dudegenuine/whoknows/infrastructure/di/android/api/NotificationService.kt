package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.INotificationService
import com.dudegenuine.whoknows.MainActivity
import com.dudegenuine.whoknows.R
import java.util.*

/**
 * Wed, 09 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
class NotificationService: INotificationService() {
    private val TAG = javaClass.simpleName

    override val notifyManager: NotificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun notifyBuilder(): NotificationCompat.Builder {
        NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            .also { notifyManager.createNotificationChannel(it) }

        return NotificationCompat.Builder(this, CHANNEL_ID)
    }

    private val currentTime = mutableStateOf(0.0)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.getDoubleExtra(EXACT_TIME_KEY, 0.0)
            .also { currentTime.value = it }

        timer.scheduleAtFixedRate(taskTimer, 0, 1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            onStartTimerForeground(flags)

        return super.onStartCommand(intent, flags, startId)
    }

    override val taskTimer: TimerTask = object: TimerTask() {
        private val broadcast = Intent(TIME_ACTION)

        override fun run() {
            currentTime.value--
            checkToFinish()

            sendBroadcast(broadcast
                .putExtra(EXACT_TIME_KEY, currentTime.value))
        }

        private fun checkToFinish() {
            Log.d(TAG, "run: ${currentTime.value}")

            if (currentTime.value <= 0) {
                timer.apply { cancel(); purge() }

                sendBroadcast(broadcast
                    .putExtra(FINISHED_TIME_KEY, true))

                this@NotificationService.stopSelf()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onStartTimerForeground(flags: Int){
        val activityIntent = MainActivity.createIntent(this, asString(currentTime.value))
            .apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }

        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, flags)

        val actionIntent = NotificationCompat.Action.Builder(
            R.drawable.ic_baseline_task_24, "Go Back", pendingIntent).build()

        val notifyBuild = notifyBuilder()
            .setContentTitle("The class still going")
            .setContentText("Just go back don\'t waste your time!")
            .setSmallIcon(R.drawable.ic_baseline_assignment_24)
            .setAutoCancel(true)
            .addAction(actionIntent).build()

        startForeground(FOREGROUND_TIMER_SERVICE_ID, notifyBuild)
    }
}