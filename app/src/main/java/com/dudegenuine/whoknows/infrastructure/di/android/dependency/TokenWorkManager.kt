package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import com.dudegenuine.repository.contract.dependency.local.ITokenWorkManager
import com.dudegenuine.whoknows.ux.worker.TokenWorker
import java.util.concurrent.TimeUnit

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
class TokenWorkManager: ITokenWorkManager {
    override val networkConnectedConstrains: Constraints
        get() = Constraints.Builder().build()

    override fun onTime(): OneTimeWorkRequest.Builder {
        return OneTimeWorkRequest.Builder(TokenWorker::class.java)
    }

    override fun periodicTime(duration: Long, timeUnit: TimeUnit): PeriodicWorkRequest.Builder {
        return PeriodicWorkRequest.Builder(TokenWorker::class.java, duration, timeUnit)
    }
}