package com.dudegenuine.local.api

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
        const val CHANNEL_ID = "channel ID"
        const val CHANNEL_NAME = "channel name"
        const val CHANNEL_DESC = "channel description"
    }

    val onBuilt: () -> NotificationCompat.Builder
    val onNotify: () -> Unit
}