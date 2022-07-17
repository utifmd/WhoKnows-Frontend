package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.dudegenuine.repository.contract.dependency.local.IIntentFactory
import com.dudegenuine.whoknows.ux.activity.MainActivity
import com.dudegenuine.whoknows.ux.compose.navigation.Screen

/**
 * Sat, 16 Jul 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnspecifiedImmutableFlag")
class IntentFactory(
    private val context: Context): IIntentFactory {

    override fun activityIntent(data: String, flags: Int): PendingIntent {
        val activity = MainActivity.instance(context, data).apply{
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return PendingIntent.getActivity(context, 0, activity, flags)
    }
    override fun notificationIntent(): PendingIntent {
        val notification = Intent(
            Intent.ACTION_VIEW,
            Screen.Home.Summary.Notification.uriPattern?.toUri(),
            context, MainActivity::class.java
        )
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(notification)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}