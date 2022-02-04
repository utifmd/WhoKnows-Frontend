package com.dudegenuine.local.manager

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import com.dudegenuine.local.manager.contract.IBroadcastReceiverManager
import kotlin.math.roundToInt

/**
 * Wed, 02 Feb 2022
 * WhoKnows by utifmd
 **/
class BroadcastReceiverManager: IBroadcastReceiverManager() {
    private val _timerData = mutableStateOf("")
    val timerData: String = _timerData.value

    override fun onReceive(context: Context, intent: Intent) {
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)

        _timerData.value = asStringTimer(time)
    }

    override fun asStringTimer(hour: Int, minute: Int, second: Int): String =
        String.format("%02d:%02d:%02d", hour, minute, second)

    override fun asStringTimer(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return asStringTimer(hours, minutes, seconds)
    }
}