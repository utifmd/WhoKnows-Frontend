package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory.Companion.ACTION_CONNECTIVITY_CHANGE
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory.Companion.ACTION_FCM_TOKEN
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory.Companion.DISCONNECTED
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory.Companion.ETHERNET_CONNECTED
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory.Companion.MOBILE_CONNECTED
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory.Companion.WIFI_CONNECTED
import com.dudegenuine.repository.contract.dependency.local.ITimerService

/**
 * Wed, 16 Mar 2022
 * WhoKnows by utifmd
 **/
class ReceiverFactory(
    override val connectionIntent:
        IntentFilter = IntentFilter(ACTION_CONNECTIVITY_CHANGE),

    override val timerIntent:
        IntentFilter = IntentFilter(ITimerService.TIME_ACTION),

    override val messagingIntent:
        IntentFilter = IntentFilter(ACTION_FCM_TOKEN)) : IReceiverFactory {
    private val TAG: String = javaClass.simpleName

    override val messagingReceiver: ((token: String) -> Unit) -> BroadcastReceiver = { onReceived ->
        receiver { _, intent ->
            intent.getStringExtra(
                IReceiverFactory.INITIAL_FCM_TOKEN)?.let(onReceived)
        }
    }

    override val timerReceiver: ((Double, Boolean) -> Unit) -> BroadcastReceiver = { onReceived ->
        receiver { _, intent ->
            val time = intent.getDoubleExtra(
                ITimerService.INITIAL_TIME_KEY, 0.0)

            val finished = intent.getBooleanExtra(
                ITimerService.TIME_UP_KEY, false)

            Log.d(TAG, "onReceived: $time")
            onReceived(time, finished)
        }
    }

    override val connectionReceiver: ((String) -> Unit) -> BroadcastReceiver = { onConnected ->
        receiver { context, _ -> registerNetwork(context, onConnected) }
    }

    private fun registerNetwork(context: Context?, onReceived: (String) -> Unit) {
        var result = DISCONNECTED
        val connectivityManager = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)

            result = when {
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> WIFI_CONNECTED
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> MOBILE_CONNECTED
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> ETHERNET_CONNECTED
                else -> DISCONNECTED
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> WIFI_CONNECTED
                        ConnectivityManager.TYPE_MOBILE -> MOBILE_CONNECTED
                        ConnectivityManager.TYPE_ETHERNET ->  ETHERNET_CONNECTED
                        else -> DISCONNECTED
                    }

                }
            }
        }

        onReceived(result)

        /*val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo

        event.onInternetStatusReceived(networkInfo != null && networkInfo.isConnectedOrConnecting)*/
    }

    private val receiver: (
        ((context: Context?, intent: Intent) -> Unit) -> BroadcastReceiver) = { onReceived ->

        object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                onReceived(context, intent)
            }
        }
    }
}