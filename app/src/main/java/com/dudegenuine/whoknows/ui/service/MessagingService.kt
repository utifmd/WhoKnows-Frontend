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
import com.dudegenuine.model.common.ImageUtil.asBitmapAsync
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
        // TODO: 3. notification go back still appear after timer counting
        // TODO: 4. delete group messaging when room deleted

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
        val largeIcon = message.data["largeIcon"] ?: message.notification?.imageUrl as String?

        val intent = MainActivity.createIntent(this, MESSAGE_INTENT).apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }
        val channelId = if (message.data.isNotEmpty()) CHANNEL_ID_JOINED else message.notification?.channelId ?: CHANNEL_ID_COMMON
        val channelLevel = if (message.data.isNotEmpty()) IMPORTANCE_MAX else IMPORTANCE_DEFAULT
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        val builder = notifier.onBuilt(channelId, channelLevel)
        with (builder) {
            if (message.data.isNotEmpty()) priority = NotificationCompat.PRIORITY_MAX

            largeIcon?.let { url -> scope.launch {
                setLargeIcon(asBitmapAsync(this@MessagingService, url)) }
            }

            setSmallIcon(R.drawable.ic_baseline_assignment_24)
            setContentIntent(pendingIntent)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
        }

        notifier.manager.notify(
            Random.nextInt(), builder.build())
    }
}

/*
{
    "notification": {
        "title": "Informasi umum 10",
        "body": "Click send to get a response"
    },
    "to": "/topics/common"
}

Authorization:key=AAAAfd49hCg:APA91bEENFcJbmG1JAKM_KRoCVmimDpWXPxExfZ1Y6mG3gsy8nIyL3Z-eepeP6iBh--ZOI_tX4Z7nsKyxqWV9fQMIkDkiBS07Oyb8_0hvZpc4WAjynfrFlmyVYeyLdqYz1BgRWR7krC8
Content-Type:application/json
project_id:540599485480
{
   "operation": "add",
   "notification_key_name": "appUser-Chris",
   "notification_key": "APA91bGHXQBB...9QgnYOEURwm0I3lmyqzk2TXQ",
   "registration_ids": ["bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."]
}
 * */

/*{
    "operation": "create",
    "notification_key_name": "anyone-knows-room-soekarno",
    "registration_ids": ["f3JltvqMT36wuO3SuWluVU:APA91bGEcLlXiP0mEhLv0fE_2ILs4IfRyO5oD8hluRZJuIlr9Z1vQIjwcmw5Z3ZqT9yD0BabMLLcwvRYiQ1QdBCPr3NCWJAEPuYW_mB0ZTegsdMaA4yeHgw9R2RhI2WNYNnUD4CEjBj0"]
}*/

/*
// response
{
    "notification_key": "APA91bG4FV29anQHxAiClb4IGUha3MxvEiGjye1xLXnARyYuEzQcYVicsW4432fSrmhQUBIcgVED4Ikx9ifufuuc3wQzSmBuxkg1iTHQVfcDdsoB88dvOnOhagNgZeXnGfxkHUhv4CMJ"
}

{
    "data": {
        "title": "Capture requst and cookies 8",
        "body": "Click send to get a response"
    },
    "to": "APA91bG4FV29anQHxAiClb4IGUha3MxvEiGjye1xLXnARyYuEzQcYVicsW4432fSrmhQUBIcgVED4Ikx9ifufuuc3wQzSmBuxkg1iTHQVfcDdsoB88dvOnOhagNgZeXnGfxkHUhv4CMJ"
}

//Rwsponse
{
    "success": 1,
    "failure": 0
}
*/