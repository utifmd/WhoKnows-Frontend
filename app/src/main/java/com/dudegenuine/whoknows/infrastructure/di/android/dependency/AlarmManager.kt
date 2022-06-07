package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock
import com.dudegenuine.repository.contract.dependency.local.IAlarmManager
import com.dudegenuine.whoknows.ux.receiver.AlarmReceiver

/**
 * Sun, 29 May 2022
 * WhoKnows by utifmd
 **/
class AlarmManager(
    private val context: Context): IAlarmManager {
    private val TAG: String = javaClass.simpleName

    override val alarmManager: AlarmManager
        get() = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setupMillis(millis: Long) {
        val triggerAtMillis = SystemClock.elapsedRealtime() + 60 * millis

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            AlarmReceiver.createInstance(context)
        )
    }
}