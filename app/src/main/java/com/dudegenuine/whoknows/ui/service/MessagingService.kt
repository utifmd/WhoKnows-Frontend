package com.dudegenuine.whoknows.ui.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.common.singleton.NotifyManager
import com.dudegenuine.whoknows.infrastructure.common.singleton.PrefsManager
import com.dudegenuine.whoknows.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class MessagingService: FirebaseMessagingService() {
    private val TAG: String = javaClass.simpleName

    companion object {
        const val INITIAL_INTENT_DATA = "MessagingNotificationService"

        const val ACTION_FCM_TOKEN = "action_fcm_token"
        const val INITIAL_FCM_TOKEN = "initial_fcm_token"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")

        Intent(ACTION_FCM_TOKEN)
            .apply { putExtra(INITIAL_FCM_TOKEN, token) }.apply(::sendBroadcast)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val prefs = PrefsManager.getInstance(this)
        val notifier = NotifyManager.getInstance(this)
        Log.d(TAG, "onMessageReceived: triggered")

        val currentUserId = prefs.read(CURRENT_USER_ID)
        Log.d(TAG, "currentUserId: $currentUserId")

        if (currentUserId.isBlank()) return

        val intent: Intent = MainActivity.createIntent(this, INITIAL_INTENT_DATA)
            .apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }

        val pending: PendingIntent = PendingIntent
            .getActivity(this, 0, intent, FLAG_ONE_SHOT)

        val title = message.data["title"] ?: message.notification?.title ?:
            getString(R.string.notify_title)

        val body = message.data["body"] ?: message.notification?.body ?:
            getString(R.string.notify_body)

        with (notifier.scaffold()) {
            setSmallIcon(R.drawable.ic_baseline_assignment_24)
            setContentTitle(title)
            setContentText(body)
            setContentIntent(pending)
            setAutoCancel(true)
        }

        notifier.notify()
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