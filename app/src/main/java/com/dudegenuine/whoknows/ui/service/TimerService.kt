package com.dudegenuine.whoknows.ui.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.ITimerService
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/**
 * Wed, 09 Feb 2022
 * WhoKnows by utifmd
 **/
@AndroidEntryPoint
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
class TimerService: ITimerService() {
    private val TAG = javaClass.simpleName

    companion object {
        fun createInstance(context: Context, time: Double): Intent{
            return Intent(context, TimerService::class.java).apply {
                putExtra(INITIAL_TIME_KEY, time)
            }
        }
        const val TIME_UP = "time up"
        const val TIME_RUNNING = "time running"
    }

    @Inject
    lateinit var notifier: INotifyManager

    @Inject
    lateinit var prefs: IPreferenceManager

    private val currentTime = mutableStateOf(0.0)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.getDoubleExtra(INITIAL_TIME_KEY, 0.0)
            .also { currentTime.value = it }

        timer.scheduleAtFixedRate(taskTimerListener(this), 0, 1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            onStartTimerForeground(flags)

        return super.onStartCommand(intent, flags, startId)
    }

    override val taskTimerListener: (Context) -> TimerTask = { context ->
        val broadcast = Intent(TIME_ACTION)

        val checkToFinish: () -> Unit = {
            Log.d(TAG, "run: ${currentTime.value}")

            if (currentTime.value <= 0.0) {
                timer.apply { cancel(); purge() }

                sendBroadcast(broadcast.putExtra(
                    TIME_UP_KEY, true))

                this@TimerService.stopSelf()
            }
        }

        val exposeAndStore: () -> Unit = {
            currentTime.value.let {
                sendBroadcast(broadcast.putExtra(
                    INITIAL_TIME_KEY, it))

                prefs.write(
                    INITIAL_TIME_KEY, it.toString()
                )
            }
        }

        object : TimerTask() {
            override fun run() {
                currentTime.value--

                checkToFinish()
                exposeAndStore()
            }
        }
    }

    private fun onStartTimerForeground(flags: Int){
        val activityIntent: Intent = MainActivity.createIntent(this, TIME_RUNNING)
            .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, flags)

        val actionIntent = NotificationCompat.Action.Builder(
            R.drawable.ic_baseline_task_24, "Go Back", pendingIntent).build()

        val builder = notifier.onBuilt()

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