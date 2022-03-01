package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_DESC
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_ID
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_NAME
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
        if (isOreoCompatibility) {
            channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH).apply {
                description = CHANNEL_DESC
                lockscreenVisibility = VISIBILITY_PUBLIC

                setShowBadge(true)
            }
        }

        manager.apply {
            createNotificationChannel(channel)
        }
    }

    override val onBuilt: () -> NotificationCompat.Builder =
        { builder }

    override val onNotify: () -> Unit =
        { manager.notify(notifyId, onBuilt().build()) }
}