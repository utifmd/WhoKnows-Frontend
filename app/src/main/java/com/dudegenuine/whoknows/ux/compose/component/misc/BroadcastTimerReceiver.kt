package com.dudegenuine.whoknows.ux.compose.component.misc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.dudegenuine.repository.contract.dependency.local.ITimerService.Companion.INITIAL_TIME_KEY
import com.dudegenuine.repository.contract.dependency.local.ITimerService.Companion.TIME_UP_KEY
import com.dudegenuine.repository.contract.dependency.local.ITimerService.Companion.TIME_ACTION
import com.dudegenuine.repository.contract.dependency.local.ITimerService.Companion.asString

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
                val time = intent.getDoubleExtra(INITIAL_TIME_KEY, 0.0)
                val finished = intent.getBooleanExtra(TIME_UP_KEY, false)

                currentTimeEvent(asString(time), finished)
            }
        }

        context.let {
            it.registerReceiver(broadcast, intentFilter)

            onDispose { it.unregisterReceiver(broadcast) }
        }
    }
}