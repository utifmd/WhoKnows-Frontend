package com.dudegenuine.whoknows.ux.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory

/**
 * Sun, 17 Jul 2022
 * WhoKnows by utifmd
 **/
class InternetStatusReceiver
    constructor(): BroadcastReceiver() {
    private val TAG: String = javaClass.simpleName
    private lateinit var forward: (String) -> Unit

    constructor(onForward: (String) -> Unit): this() {
        forward = onForward
    }
    companion object {
        fun instance() = IntentFilter(IReceiverFactory.ACTION_CONNECTIVITY_CHANGE)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: trigger")
        checkConnectivity(context, forward)
    }
    private fun checkConnectivity(context: Context?, onReceived: (String) -> Unit) {
        var result = IReceiverFactory.DISCONNECTED
        val connectivityManager = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)

            result = when {
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> IReceiverFactory.WIFI_CONNECTED
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> IReceiverFactory.MOBILE_CONNECTED
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> IReceiverFactory.ETHERNET_CONNECTED
                else -> IReceiverFactory.DISCONNECTED
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> IReceiverFactory.WIFI_CONNECTED
                        ConnectivityManager.TYPE_MOBILE -> IReceiverFactory.MOBILE_CONNECTED
                        ConnectivityManager.TYPE_ETHERNET -> IReceiverFactory.ETHERNET_CONNECTED
                        else -> IReceiverFactory.DISCONNECTED
                    }

                }
            }
        }
        onReceived(result)
    }
}

/*val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
val networkInfo = connMgr.activeNetworkInfo

event.onInternetStatusReceived(networkInfo != null && networkInfo.isConnectedOrConnecting)*/