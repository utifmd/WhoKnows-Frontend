package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dudegenuine.model.common.Utility.isOreoCompatibility
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import com.dudegenuine.repository.contract.dependency.local.INotifyManager.Companion.CHANNEL_ID_TIMER
import com.dudegenuine.whoknows.R

/**
 * Wed, 16 Feb 2022
 * WhoKnows by utifmd
 **/
class NotifyManager(
    context: Context): INotifyManager {
    override var manager: NotificationManagerCompat = NotificationManagerCompat.from(context)
    override var builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID_TIMER)
    override lateinit var channel: NotificationChannel

    override val onBuilt: (String, Int) -> NotificationCompat.Builder = { channelId, importance ->
        if (isOreoCompatibility) {
            channel = NotificationChannel(channelId, context.getString(R.string.app_name), importance).apply {
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

                setShowBadge(true)
            }

            manager.createNotificationChannel(channel)
        }

        builder = NotificationCompat.Builder(context, channelId)
        builder
    }
}