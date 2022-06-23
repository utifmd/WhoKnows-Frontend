package com.dudegenuine.repository.contract.dependency.local

import android.app.AlarmManager

/**
 * Sun, 29 May 2022
 * WhoKnows by utifmd
 **/
interface IAlarmManager {
    val alarmManager: AlarmManager

    fun setupMillis(millis: Long)
    fun setupMinute(minute: Int)
    fun cancel()
    //fun isRoomAlarmUp(): Boolean

    companion object {
        const val RECEIVER_ALARM_KEY = "RECEIVER_ALARM_KEY"
    }
}

/*
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    ...
    alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
        PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    alarmMgr?.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 60 * 1000,
            alarmIntent
    )
    */