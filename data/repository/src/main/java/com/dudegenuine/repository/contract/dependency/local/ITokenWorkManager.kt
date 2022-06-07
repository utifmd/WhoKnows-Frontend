package com.dudegenuine.repository.contract.dependency.local

import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
interface ITokenWorkManager {
    val networkConnectedConstrains: Constraints

    fun onTime(): OneTimeWorkRequest.Builder
    fun periodicTime(duration: Long, timeUnit: TimeUnit): PeriodicWorkRequest.Builder
}