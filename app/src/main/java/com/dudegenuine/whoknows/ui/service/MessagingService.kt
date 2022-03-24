package com.dudegenuine.whoknows.ui.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_ID_COMMON
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_ID_JOINED
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_NOTIFICATION_BADGE
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.local.api.IReceiverFactory.Companion.ACTION_FCM_TOKEN
import com.dudegenuine.local.api.IReceiverFactory.Companion.INITIAL_FCM_TOKEN
import com.dudegenuine.model.common.ImageUtil.getBitmapAsync
import com.dudegenuine.repository.contract.IMessagingRepository.Companion.MESSAGING_TOKEN
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.activity.MainActivity
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MessagingService: FirebaseMessagingService() {
    private val TAG: String = javaClass.simpleName
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    @Inject lateinit var prefs: IPreferenceManager
    @Inject lateinit var notifier: INotifyManager

    companion object {
        const val MESSAGE_INTENT = "MessagingService message intent"
    }

    private val notify: (Notification) -> Unit = {
        notifier.manager.notify(Random.nextInt(), it)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "onNewToken: $token")
        prefs.write(MESSAGING_TOKEN, token)

        Intent(ACTION_FCM_TOKEN)
            .apply { putExtra(INITIAL_FCM_TOKEN, token) }.apply(::sendBroadcast)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val currentUserId = prefs.readString(CURRENT_USER_ID)

        Log.d(TAG, "onMessageReceived: triggered")
        message.notification?.let(::notification)

        if (currentUserId.isBlank()) return
        notification(message.data)
    }

    private fun notification(data: RemoteMessage.Notification){
        val notifierIntent = MainActivity.createInstance(this, MESSAGE_INTENT)
            .apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }

        val notifierPendingIntent = PendingIntent
            .getActivity(this, 0, notifierIntent, FLAG_ONE_SHOT)

        val title = data.title ?: getString(R.string.notify_title)
        val body = data.body ?: getString(R.string.notify_body)

        val largeIcon = getDrawable(R.mipmap.ic_launcher)?.toBitmap()
        val channelId = data.channelId ?: CHANNEL_ID_COMMON

        with (notifier.onBuilt(channelId, IMPORTANCE_DEFAULT)) {
            priority = IMPORTANCE_DEFAULT
            largeIcon?.let(::setLargeIcon)

            setSmallIcon(R.drawable.ic_outline_fact_check_24)
            setContentIntent(notifierPendingIntent)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)

            notify(build())
        }
    }

    private fun notification(data: Map<String, String>) {
        val dataIntent = Intent(Intent.ACTION_VIEW,
            Screen.Home.Discover.Notification.uriPattern?.toUri(),
            this, MainActivity::class.java)

        val dataPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(dataIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT) }

        val title = data["title"] ?: getString(R.string.notify_title)
        val body = data["body"] ?: getString(R.string.notify_body)
        val largeIcon = data["largeIcon"]
        val currentBadge = prefs.readInt(CURRENT_NOTIFICATION_BADGE)

        prefs.write(CURRENT_NOTIFICATION_BADGE, currentBadge +1)
        with (notifier.onBuilt(CHANNEL_ID_JOINED, IMPORTANCE_MAX)) {
            priority = NotificationCompat.PRIORITY_MAX

            setSmallIcon(R.drawable.ic_outline_fact_check_24)
            setContentIntent(dataPendingIntent)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)

            if (largeIcon != null) scope.launch {
                getBitmapAsync(this@MessagingService, largeIcon) { setLargeIcon(it); notify(build()) }
            } else notify(build())
        }
    }
}