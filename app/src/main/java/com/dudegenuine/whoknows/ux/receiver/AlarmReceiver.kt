package com.dudegenuine.whoknows.ux.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import com.dudegenuine.repository.contract.dependency.local.IAlarmManager.Companion.RECEIVER_ALARM_KEY
import com.dudegenuine.repository.contract.dependency.local.IIntentFactory
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import com.dudegenuine.whoknows.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

/**
 * Mon, 30 May 2022
 * WhoKnows by utifmd
 **/
@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    private val TAG: String = javaClass.simpleName
    @Inject lateinit var notifier: INotifyManager
    @Inject lateinit var intentFactory: IIntentFactory

    override fun onReceive(context: Context?, intent: Intent?) {
        val roomAlarm = intent?.getBooleanExtra(RECEIVER_ALARM_KEY, false) ?: return

        Log.d(TAG, "onReceive: $roomAlarm")
        if (context == null) return
        notification(context,
            intentFactory.activityIntent("onReceive $roomAlarm", PendingIntent.FLAG_UPDATE_CURRENT))
    }
    private fun notification(context: Context, pendingIntent: PendingIntent){
        val builder = notifier.onBuilt(
            INotifyManager.CHANNEL_ID_ALARM,
            NotificationManagerCompat.IMPORTANCE_MAX
        )
        with(builder) {
            priority = NotificationCompat.PRIORITY_MAX

            setContentTitle(context.getString(R.string.notification_title_alarm_receiver))
            setContentText(context.getString(R.string.notification_body_alarm_receiver))
            setSmallIcon(R.drawable.ic_outline_fact_check_24)
            getDrawable(context, R.mipmap.ic_launcher)?.toBitmap()?.let(::setLargeIcon)
            setAutoCancel(true)
            setContentIntent(pendingIntent)

            notifier.manager.notify(Random.nextInt(), build())
        }
    }
    companion object {
        internal fun pendingIntent(context: Context, value: Boolean): PendingIntent =
            Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.putExtra(RECEIVER_ALARM_KEY, value)
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}