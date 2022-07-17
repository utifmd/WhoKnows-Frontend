package com.dudegenuine.whoknows.ux.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.repository.contract.dependency.local.INotifyManager.Companion.CHANNEL_ID_TIMER
import com.dudegenuine.whoknows.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/**
 * Wed, 09 Feb 2022
 * WhoKnows by utifmd
 **/
@AndroidEntryPoint
class TimerService: ITimerService() {
    private val TAG = javaClass.simpleName
    @Inject lateinit var notifier: INotifyManager
    @Inject lateinit var resource: IResourceDependency
    @Inject lateinit var intentFactory: IIntentFactory
    @Inject lateinit var prefsFactory: IPrefsFactory
    private val currentTime = mutableStateOf(0.0)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.getDoubleExtra(INITIAL_TIME_KEY, 0.0).also { currentTime.value = it }

        timer.scheduleAtFixedRate(taskTimerListener(this), 0, 1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            onStartTimerForeground(flags)

        return super.onStartCommand(intent, flags, startId)
    }

    override val taskTimerListener: (Context) -> TimerTask = { //context ->
        val broadcast = Intent(TIME_ACTION)

        val checkToFinish: () -> Unit = {
            Log.d(TAG, "run: ${currentTime.value}")

            if (currentTime.value <= 0.0) {
                timer.apply { cancel(); purge() }
                sendBroadcast(broadcast.putExtra(TIME_UP_KEY, true))

                this@TimerService.stopSelf()
            }
        }

        val exposeAndStore: () -> Unit = {
            currentTime.value.let {
                sendBroadcast(broadcast.putExtra(INITIAL_TIME_KEY, it))

                onRunningTimeChange(it)
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
        val intent = intentFactory.activityIntent(TIME_RUNNING, flags)
        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_baseline_task_24, getString(R.string.go_back), intent)

        val builder = notifier.onBuilt(CHANNEL_ID_TIMER, IMPORTANCE_DEFAULT)

        with(builder) {
            setContentTitle(getString(R.string.notification_title_timer_service))
            setContentText(getString(R.string.notification_body_timer_service))
            setSmallIcon(R.drawable.ic_outline_timer_24)
            setAutoCancel(true)
            addAction(action.build())
            setLargeIcon(resource.appIcon)
        }

        startForeground(FOREGROUND_TIMER_SERVICE_ID, builder.build())
    }

    private fun onRunningTimeChange(fresh: Double) {
        prefsFactory.participationTimeLeft = fresh.toInt()
    }

    /*companion object {
        fun createInstance(context: Context, time: Double): Intent{
            return Intent(context, TimerService::class.java).apply {
                putExtra(INITIAL_TIME_KEY, time)
            }
        }
    }*/
}