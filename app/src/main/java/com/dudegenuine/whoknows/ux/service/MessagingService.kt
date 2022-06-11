package com.dudegenuine.whoknows.ux.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.ImageUtil.getBitmapAsync
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import com.dudegenuine.repository.contract.dependency.local.INotifyManager.Companion.CHANNEL_ID_COMMON
import com.dudegenuine.repository.contract.dependency.local.INotifyManager.Companion.CHANNEL_ID_JOINED
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.usecase.messaging.GetMessaging
import com.dudegenuine.usecase.messaging.RemoveMessaging
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.activity.MainActivity
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@AndroidEntryPoint
class MessagingService: FirebaseMessagingService() {
    @Inject lateinit var prefsFactory: IPrefsFactory
    @Inject lateinit var notifier: INotifyManager
    @Inject lateinit var repository: IMessagingRepository
    private val TAG: String = javaClass.simpleName
    private val messagingJob = SupervisorJob()
    private val messagingScope = CoroutineScope(Dispatchers.Main + messagingJob)
    companion object { const val MESSAGE_INTENT = "MessagingService message intent" }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        onTokenIdChange(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, "onMessageReceived: triggered")
        message.notification?.let(::notification)

        if (prefsFactory.userId.isBlank()) return
        notification(message.data)
    }

    private val notify: (Notification) -> Unit = {
        notifier.manager.notify(Random.nextInt(), it)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notification(data: RemoteMessage.Notification){
        val notifierIntent = MainActivity.createInstance(this, MESSAGE_INTENT)
            .apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }

        val notifierPendingIntent = PendingIntent
            .getActivity(this, 0, notifierIntent, FLAG_ONE_SHOT)

        val title = data.title ?: getString(R.string.app_name)
        val body = data.body ?: return //getString(R.string.notify_body)

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
            Screen.Home.Summary.Notification.uriPattern?.toUri(),
            this, MainActivity::class.java)

        val dataPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(dataIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT) }

        val title = data["title"] ?: return
        val body = data["body"] ?: return
        val largeIcon = data["largeIcon"]

        onRemoveMessaging(data["args"])

        // TODO: make intent for this //onBadgeChange(prefsFactory.notificationBadge +1)

        with (notifier.onBuilt(CHANNEL_ID_JOINED, IMPORTANCE_MAX)) {
            priority = NotificationCompat.PRIORITY_MAX

            setSmallIcon(R.drawable.ic_outline_fact_check_24)
            setContentIntent(dataPendingIntent)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)

            if (largeIcon.isNullOrBlank()) notify(build())
            else messagingScope.launch {
                getBitmapAsync(this@MessagingService, largeIcon)
                    { setLargeIcon(it); notify(build()) }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun onRemoveMessaging(args: String?) {
        val caseGetMessaging = GetMessaging(repository)
        val caseRemoveMessaging = RemoveMessaging(repository)

        if (args.isNullOrBlank()) return
        val elem = JsonParser.parseString(args)

        if (elem !is JsonObject) return
        if (!elem.has("roomId")) return
        if (!elem.has("userId")) return
        val roomId = elem["roomId"].asString
        val userId = elem["userId"].asString

        if (userId == prefsFactory.userId) caseGetMessaging(roomId)
            .flatMapConcat { res -> when (res) {
                is Resource.Success -> res.data?.let { notification_key ->
                    val remover = Messaging.GroupRemover(
                        roomId, listOf(prefsFactory.tokenId), notification_key)

                    caseRemoveMessaging(remover)
                } ?: emptyFlow()
                else -> emptyFlow() }}
            .onEach{ Log.d(TAG, "onRemoveMessaging: ${it.data}") }
            .onEmpty{ Log.d(TAG, "onRemoveMessaging: res.data unsuccessful or null") }
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(messagingScope)

        Log.d(TAG, "onRemoveMessaging: $roomId")
    }

    private fun onTokenIdChange(token: String) {
        prefsFactory.tokenId = token
    }

    /*private fun onBadgeChange(fresh: Int) {
        prefsFactory.notificationBadge = fresh
    }*/

    override fun onDestroy() {
        super.onDestroy()
        messagingJob.cancel()
    }
}

/*
//GET https://fcm.googleapis.com/fcm/notification?notification_key_name=ROM-1001
//POST
{
    "operation": "create",
    "notification_key_name": "TEST-ROM",
    "registration_ids": ["cBOpf7kEQuCyup70DcfGKX:APA91bF8y10huDQJPBINrp9vfzk8ZzdvWe6PWMpqIebxYFCLaAm19Xcmz82UkSmsC70syLFdwYwJN8v7kLAC2rKbW40JoI16KNictf9qyGnVe74oqpiXorDaZ5MwQ3nSiwdcnZbyQ4Da"]
}
//POST
{
    "operation": "add",
    "notification_key_name": "TEST-ROM",
    "notification_key": "APA91bH_KBq4P88tY7r5alA8-yojPecp_QMcmDmJQVhwIStsTr_K3q0N3NEJI31tTz55EE9NT9_ekSeqSmRBMPcF_LT8-S_c0YzFvGgtqtr7j2QoV7bJl7w",
    "registration_ids": ["cBOpf7kEQuCyup70DcfGKX:APA91bF8y10huDQJPBINrp9vfzk8ZzdvWe6PWMpqIebxYFCLaAm19Xcmz82UkSmsC70syLFdwYwJN8v7kLAC2rKbW40JoI16KNictf9qyGnVe74oqpiXorDaZ5MwQ3nSiwdcnZbyQ4Da"]
}
//POST
{
    "operation": "remove",
    "notification_key_name": "TEST-ROM",
    "notification_key": "APA91bH_KBq4P88tY7r5alA8-yojPecp_QMcmDmJQVhwIStsTr_K3q0N3NEJI31tTz55EE9NT9_ekSeqSmRBMPcF_LT8-S_c0YzFvGgtqtr7j2QoV7bJl7w",
    "registration_ids": ["cBOpf7kEQuCyup70DcfGKX:APA91bF8y10huDQJPBINrp9vfzk8ZzdvWe6PWMpqIebxYFCLaAm19Xcmz82UkSmsC70syLFdwYwJN8v7kLAC2rKbW40JoI16KNictf9qyGnVe74oqpiXorDaZ5MwQ3nSiwdcnZbyQ4Da"]
}
//POST
{
    "data": {
        "title": "Specific Target Messaging",
        "body": "Click send to get a response lorem ipsum.",
        "largeIcon": ""
    },
    "to": "APA91bH_KBq4P88tY7r5alA8-yojPecp_QMcmDmJQVhwIStsTr_K3q0N3NEJI31tTz55EE9NT9_ekSeqSmRBMPcF_LT8-S_c0YzFvGgtqtr7j2QoV7bJl7w"
}
*/