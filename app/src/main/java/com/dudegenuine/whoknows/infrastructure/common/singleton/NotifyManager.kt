package com.dudegenuine.whoknows.infrastructure.common.singleton

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dudegenuine.model.common.Utility.isOreoCompatibility
import kotlin.random.Random

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
class NotifyManager
    private constructor(context: Context){
    private val notifyId = Random.nextInt()
    private lateinit var channel: NotificationChannel

    private var manager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    private var builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)

    companion object: SingletonHolder<NotifyManager, Context>(::NotifyManager) {
        const val CHANNEL_ID = "channel ID"
        const val CHANNEL_NAME = "channel name"
    }

    init {
        if (isOreoCompatibility) {
            channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        }

        manager.apply {
            createNotificationChannel(channel)
        }
    }

    val scaffold: () -> NotificationCompat.Builder = { builder }

    val notify: () -> Unit = {
        manager.notify(notifyId, scaffold().build())
    }
}