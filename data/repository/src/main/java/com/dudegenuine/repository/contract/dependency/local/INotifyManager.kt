package com.dudegenuine.repository.contract.dependency.local

import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Wed, 16 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotifyManager {
    var manager: NotificationManagerCompat // = NotificationManagerCompat.from(context)
    var channel: NotificationChannel
    var builder: NotificationCompat.Builder // = NotificationCompat.Builder(context, CHANNEL_ID)

    companion object {
        const val CHANNEL_ID_ALARM = "alarm"
        const val CHANNEL_ID_TIMER = "timer"
        const val CHANNEL_ID_COMMON = "common"
        const val CHANNEL_ID_JOINED = "joined"
    }

    val onBuilt: (String, Int) -> NotificationCompat.Builder
    //val onNotify: () -> Unit
}