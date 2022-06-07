package com.dudegenuine.whoknows.ux.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dudegenuine.repository.contract.dependency.local.IAlarmManager.Companion.RECEIVER_ALARM_KEY
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Mon, 30 May 2022
 * WhoKnows by utifmd
 **/
@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    private val TAG: String = javaClass.simpleName
    @Inject lateinit var notifier: INotifyManager

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarm = intent?.getStringExtra(RECEIVER_ALARM_KEY) ?: return

        Log.d(TAG, "onReceive: $alarm")
    }

    companion object {
        internal fun createInstance(context: Context): PendingIntent =
            Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.putExtra(RECEIVER_ALARM_KEY, "example parameter value")
                PendingIntent.getBroadcast(context, 0, intent, 0)
        }
    }
}