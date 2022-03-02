package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_ID
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_NAME
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_PARAM_MAX
import com.dudegenuine.model.common.Utility.isOreoCompatibility
import kotlin.random.Random

/**
 * Wed, 16 Feb 2022
 * WhoKnows by utifmd
 **/
class NotifyManager(
    context: Context): INotifyManager {
    override lateinit var channel: NotificationChannel

    override var manager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override var builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)

    private val notifyId = Random.nextInt()

    init {
        if (isOreoCompatibility) manager.apply {
            channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)

            createNotificationChannel(channel)
        }
    }

    override val onBuilt: (String) -> NotificationCompat.Builder = { channelParam ->
        if (isOreoCompatibility) {
            channel = NotificationChannel(
                CHANNEL_ID+channelParam,
                CHANNEL_NAME+channelParam,
                if (channelParam == CHANNEL_PARAM_MAX) IMPORTANCE_HIGH
                else IMPORTANCE_DEFAULT
            ).apply {
                description = INotifyManager.CHANNEL_DESC
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

                setShowBadge(true)
            }
        }

        builder
    }

    override val onNotify: () -> Unit =
        { manager.notify(notifyId, onBuilt(CHANNEL_PARAM_MAX).build()) }
}