package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.util.Log
import com.dudegenuine.repository.contract.dependency.local.IAlarmManager
import com.dudegenuine.whoknows.ux.receiver.AlarmReceiver
import java.util.*

/**
 * Sun, 29 May 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnspecifiedImmutableFlag")
class AlarmManager(
    private val context: Context): IAlarmManager {
    private val TAG: String = javaClass.simpleName
    private val roomAlarmPendingIntent = AlarmReceiver.pendingIntent(context, true)

    override val alarmManager: AlarmManager
        get() = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val calendar
        get() = Calendar.getInstance()

    override fun cancel(){ alarmManager.cancel(roomAlarmPendingIntent) }
    override fun setupMillis(millis: Long) {
        Log.d(TAG, "setupMillis: $millis")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            millis,
            roomAlarmPendingIntent
        )
    }
    override fun setupMinute(minute: Int) {
//        calendar.set(
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//        calendar.set(Calendar.MINUTE, 15)
        setupMillis(minute.toLong() * 60 * 1000)
    }
}