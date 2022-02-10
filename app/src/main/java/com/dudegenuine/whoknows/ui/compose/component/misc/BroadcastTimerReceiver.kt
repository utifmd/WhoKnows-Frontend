package com.dudegenuine.whoknows.ui.compose.component.misc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.dudegenuine.local.api.INotificationService.Companion.EXACT_TIME_KEY
import com.dudegenuine.local.api.INotificationService.Companion.FINISHED_TIME_KEY
import com.dudegenuine.local.api.INotificationService.Companion.TIME_ACTION
import com.dudegenuine.local.api.INotificationService.Companion.asString

@Composable
fun BroadcastTimerReceiver(
    action: String = TIME_ACTION,
    onTimeEvent: (String, Boolean) -> Unit){

    val context = LocalContext.current
    val currentTimeEvent by rememberUpdatedState(onTimeEvent)

    DisposableEffect(context, action){
        val intentFilter = IntentFilter(action)
        val broadcast = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent) {
                val time = intent.getDoubleExtra(EXACT_TIME_KEY, 0.0)
                val finished = intent.getBooleanExtra(FINISHED_TIME_KEY, false)

                currentTimeEvent(asString(time), finished)
            }
        }

        context.let {
            it.registerReceiver(broadcast, intentFilter)

            onDispose { it.unregisterReceiver(broadcast) }
        }
    }
}