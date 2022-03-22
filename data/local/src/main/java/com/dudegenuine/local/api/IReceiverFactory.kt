package com.dudegenuine.local.api

import android.content.BroadcastReceiver

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

    val timerReceived: ((time: Double, finished: Boolean) -> Unit) -> BroadcastReceiver
    val networkReceived: ((connected :String) -> Unit) -> BroadcastReceiver
    val tokenReceived: ((token: String) -> Unit) -> BroadcastReceiver

    /*fun timerReceiver(): BroadcastReceiver
    fun internetStatusReceiver(): BroadcastReceiver*/
}