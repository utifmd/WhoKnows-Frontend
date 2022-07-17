package com.dudegenuine.usecase.messaging

import android.app.Notification
import android.app.PendingIntent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
import com.dudegenuine.model.common.Utility.EMPTY_STRING
import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import com.dudegenuine.repository.contract.dependency.local.INotifyManager.Companion.CHANNEL_ID_JOINED
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

/**
 * Sat, 16 Jul 2022
 * WhoKnows by utifmd
 **/
class RetrieveMessaging(
    private val reposNotifier: INotificationRepository){
    private val TAG: String = javaClass.simpleName
    private val notifier = reposNotifier.notifier
    private val userId get() = reposNotifier.prefs.userId
    private val resource get() = reposNotifier.resource
    private val intent get() = reposNotifier.intent
    private fun notify(it: Notification) = notifier.manager.notify(Random.nextInt(), it)

    suspend operator fun invoke(
        params: Map<String, String>, onRemoveParticipation: () -> Unit,
        onForwardNotification: (com.dudegenuine.model.Notification) -> Unit) {
        val title = params["title"] ?: return
        val body = params["body"] ?: return
        val largeIcon = params["largeIcon"]
        val args = params["args"]?.split("|")
        val notification = reposNotifier.initial.copy(
            title = title,
            event = body,
            userId = args?.component1() ?: "undefined",
            roomId = args?.component2() ?: "undefined",
            recipientId = userId,
            imageUrl = largeIcon ?: EMPTY_STRING,
            isDetail = false
        )
        if (args?.component1() != userId) {
            if (body.contains("just removed") ||
                body.contains("kicked out") ||
                body.contains("just left")) onForwardNotification(notification)
            if (body.contains("kicked out")) onRemoveParticipation()
        }
        with (notifier.onBuilt(CHANNEL_ID_JOINED, IMPORTANCE_MAX)) {
            priority = NotificationCompat.PRIORITY_MAX

            setSmallIcon(resource.statusBarSmallIconId)
            setContentIntent(intent.notificationIntent())
            setContentTitle(title)
            setContentText(body)
            setStyle(asBigStyle(body))
            setAutoCancel(true)

            fun loaded(bitmap: Bitmap){
                setLargeIcon(bitmap)
                notify(build())
            }
            if (largeIcon.isNullOrBlank()) notify(build())
            else resource.bitmapAsync(largeIcon, ::loaded) //bitmapFlow(largeIcon).onEach(::loaded).launchIn(coroutineScope)
        }
    }
    operator fun invoke(params: RemoteMessage.Notification) {
        Log.d(TAG, "invoke: $params")

        val title = params.title ?: resource.appName
        val body = params.body ?: return //getString(R.string.notify_body)
        val channelId = params.channelId ?: INotifyManager.CHANNEL_ID_COMMON

        with (notifier.onBuilt(channelId, NotificationManagerCompat.IMPORTANCE_DEFAULT)) {
            priority = NotificationManagerCompat.IMPORTANCE_DEFAULT
            resource.appIcon?.let(::setLargeIcon)

            setSmallIcon(resource.statusBarSmallIconId)
            setContentIntent(intent.activityIntent("MESSAGE_INTENT_DATA", PendingIntent.FLAG_UPDATE_CURRENT))
            setContentTitle(title)
            setContentText(body)
            setStyle(asBigStyle(body))
            setAutoCancel(true)

            notify(build())
        }
    }
    private fun asBigStyle(body: String) = NotificationCompat.BigTextStyle().bigText(body)
}