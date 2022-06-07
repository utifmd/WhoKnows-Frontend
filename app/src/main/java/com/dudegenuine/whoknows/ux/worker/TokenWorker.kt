package com.dudegenuine.whoknows.ux.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.KEY_ROOM_TOKEN_PARAM
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.KEY_ROOM_TOKEN_RESULT
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
@HiltWorker
class TokenWorker
    @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: IUserRepository): CoroutineWorker(context, params) {
    private val TAG: String = javaClass.simpleName

    override suspend fun doWork(): Result {
        Log.d(TAG, "suspend doWork: begin..")

        val currentUser = userRepository.localRead()
        val token = inputData.getString(KEY_ROOM_TOKEN_PARAM) ?: return Result.failure()

        userRepository.remoteUpdate(currentUser.id, currentUser.copy(tokens = listOf(token)))
        return Result.success(workDataOf(
            KEY_ROOM_TOKEN_RESULT to "${currentUser.id} has been updated"))
    }

    companion object {
        private val connectedConstrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)

        fun oneTimeBuilder(token: String) = OneTimeWorkRequestBuilder<TokenWorker>()
            .setInputData(workDataOf(KEY_ROOM_TOKEN_PARAM to token))
            .setConstraints(connectedConstrains.build())
    }
}