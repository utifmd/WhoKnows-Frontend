package com.dudegenuine.whoknows.ui.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
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
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_ID_COMMON
import com.dudegenuine.local.api.INotifyManager.Companion.CHANNEL_ID_JOINED
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.model.common.ImageUtil.getBitmapAsync
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
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

        const val ACTION_FCM_TOKEN = "action_fcm_token"
        const val INITIAL_FCM_TOKEN = "initial_fcm_token"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")

        //prefs.write(IMessagingRepository.MESSAGING_TOKEN, token)
        // TODO: 2. route from result to profile bottom bar not found

        Intent(ACTION_FCM_TOKEN)
            .apply { putExtra(INITIAL_FCM_TOKEN, token) }.apply(::sendBroadcast)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: triggered")

        val currentUserId = prefs.read(CURRENT_USER_ID)
        Log.d(TAG, "currentUserId: $currentUserId")
        if (currentUserId.isBlank()) return

        notification(message)
    }

    private fun notification(message: RemoteMessage) {
        val title = message.data["title"] ?: message.notification?.title ?: getString(R.string.notify_title)
        val body = message.data["body"] ?: message.notification?.body ?: getString(R.string.notify_body)
        val largeIcon = message.data["largeIcon"]

        val intent = MainActivity.createIntent(this, MESSAGE_INTENT).apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }
        val channelId = if (message.data.isNotEmpty()) CHANNEL_ID_JOINED else message.notification?.channelId ?: CHANNEL_ID_COMMON
        val channelLevel = if (message.data.isNotEmpty()) IMPORTANCE_MAX else IMPORTANCE_DEFAULT
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        with (notifier.onBuilt(channelId, channelLevel)) {
            if (message.data.isNotEmpty()) priority = NotificationCompat.PRIORITY_MAX

            setSmallIcon(R.drawable.ic_outline_fact_check_24)
            setContentIntent(pendingIntent)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)

            val notify: () -> Unit = {
                notifier.manager.notify(Random.nextInt(), build()) }

            if (largeIcon != null) scope.launch {
                getBitmapAsync(this@MessagingService, largeIcon) {
                    setLargeIcon(it)
                    notify()
                }
            } else notify()
        }
    }
}