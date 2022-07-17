package com.dudegenuine.repository.contract.dependency.local

import android.app.PendingIntent

/**
 * Sat, 16 Jul 2022
 * WhoKnows by utifmd
 **/
interface IIntentFactory {
    fun activityIntent(data: String, flags: Int = 0): PendingIntent
    fun notificationIntent(): PendingIntent
}