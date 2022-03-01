package com.dudegenuine.local.api.contract

import android.app.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Mon, 28 Feb 2022
 * WhoKnows by utifmd
 **/
abstract class IServiceCoroutine: Service() {

    protected val job = SupervisorJob()
    protected val scope = CoroutineScope(Dispatchers.Main + job)
}