package com.dudegenuine.local.manager.contract

import android.content.BroadcastReceiver

/**
 * Wed, 02 Feb 2022
 * WhoKnows by utifmd
 **/
abstract class IBroadcastReceiverManager: BroadcastReceiver() {
    abstract fun asStringTimer(time: Double): String
    abstract fun asStringTimer(hour: Int, minute: Int, second: Int): String

    companion object {
        const val TIME_EXTRA = "timeExtra"
        const val TIMER_UPDATED = "timerUpdated"
    }
}