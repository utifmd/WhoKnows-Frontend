package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.whoknows.ux.receiver.InternetStatusReceiver
import com.dudegenuine.whoknows.ux.receiver.TimerReceiver
import com.dudegenuine.whoknows.ux.receiver.TokenReceiver

/**
 * Wed, 16 Mar 2022
 * WhoKnows by utifmd
 **/
class ReceiverFactory: IReceiverFactory {
    private val TAG: String = javaClass.simpleName
    override val connectionIntent = InternetStatusReceiver.instance()
    override val tokenIntent: IntentFilter = TokenReceiver.instance()
    override val timerIntent = TimerReceiver.instance()

    override fun onConnectionReceived(
        onReceived: (connected: String) -> Unit): BroadcastReceiver {
        return InternetStatusReceiver(onReceived)
    }
    override fun onTokenReceived(
        onReceived: (token: String) -> Unit): BroadcastReceiver {
        return TokenReceiver(onReceived)
    }
    override fun onTimerReceived(
        onReceived: (time: Double, finished: Boolean) -> Unit): BroadcastReceiver {
        return TimerReceiver(onReceived)
    }
}