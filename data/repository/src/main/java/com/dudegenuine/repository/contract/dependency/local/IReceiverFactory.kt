package com.dudegenuine.repository.contract.dependency.local

import android.content.BroadcastReceiver
import android.content.IntentFilter

/**
 * Wed, 16 Mar 2022
 * WhoKnows by utifmd
 **/
interface IReceiverFactory {

    companion object {
        const val ACTION_CONNECTIVITY_CHANGE ="android.net.conn.CONNECTIVITY_CHANGE"
        const val ACTION_FCM_TOKEN = "action_fcm_token"

        const val INITIAL_FCM_TOKEN = "initial_fcm_token"
        const val DISCONNECTED = "No connection"
        const val WIFI_CONNECTED = "Wifi connected"
        const val MOBILE_CONNECTED = "Cellular connected"
        const val ETHERNET_CONNECTED = "Ethernet connected"
    }
    val connectionIntent: IntentFilter
    val timerIntent: IntentFilter
    val tokenIntent: IntentFilter

    fun onConnectionReceived(onReceived: (connected :String) -> Unit): BroadcastReceiver
    fun onTokenReceived(onReceived: (token: String) -> Unit): BroadcastReceiver
    fun onTimerReceived(onReceived: (time: Double, finished: Boolean) -> Unit): BroadcastReceiver
}