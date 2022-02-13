package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.ITimerNotificationService
import com.dudegenuine.whoknows.MainActivity
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.common.singleton.NotifyManager
import java.util.*

/**
 * Wed, 09 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
class TimerNotificationService: ITimerNotificationService() {
    private val TAG = javaClass.simpleName
    private val currentTime = mutableStateOf(0.0)

    companion object {
        fun createInstance(context: Context, time: Double): Intent{
            return Intent(context, TimerNotificationService::class.java).apply {
                putExtra(INITIAL_TIME_KEY, time)
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.getDoubleExtra(INITIAL_TIME_KEY, 0.0)
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
                .putExtra(INITIAL_TIME_KEY, currentTime.value))
        }

        private fun checkToFinish() {
            Log.d(TAG, "run: ${currentTime.value}")

            if (currentTime.value <= 0) {
                timer.apply { cancel(); purge() }

                sendBroadcast(broadcast
                    .putExtra(FINISHED_TIME_KEY, true))

                this@TimerNotificationService.stopSelf()
            }
        }
    }

    private fun onStartTimerForeground(flags: Int){
        val activityIntent = MainActivity.createIntent(this, asString(currentTime.value))
            .apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }

        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, flags)

        val actionIntent = NotificationCompat.Action.Builder(
            R.drawable.ic_baseline_task_24, "Go Back", pendingIntent).build()

        val instance = NotifyManager.getInstance(this)
        val builder = instance.scaffold()

        with(builder) {
            setContentTitle("The class still going")
            setContentText("Just go back don\'t waste your time!")
            setSmallIcon(R.drawable.ic_baseline_assignment_24)
            setAutoCancel(true)
            addAction(actionIntent)
        }

        startForeground(FOREGROUND_TIMER_SERVICE_ID, builder.build())
    }
}