package com.dudegenuine.whoknows.ux.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.repository.contract.dependency.local.ITimerService

/**
 * Sun, 17 Jul 2022
 * WhoKnows by utifmd
 **/
class TokenReceiver(): BroadcastReceiver() {
    private val TAG: String = javaClass.simpleName
    private lateinit var forward: (String) -> Unit

    constructor(onForward:(String) -> Unit): this(){
        forward = onForward
    }
    companion object {
        fun instance() = IntentFilter(ITimerService.TIME_ACTION)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: trigger")
        intent?.getStringExtra(IReceiverFactory.INITIAL_FCM_TOKEN)?.let(forward)
    }
}