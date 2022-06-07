package com.dudegenuine.repository.contract.dependency.local

import androidx.work.WorkManager

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
interface IWorkerManager {
    fun instance(): WorkManager

    companion object {
        const val TAG_OUTPUT = "tag_output"
        const val TAG_ROOM_TOKEN = "TAG_ROOM_TOKEN"
        const val WORK_NAME_ROOM_TOKEN = "WORK_NAME_ROOM_TOKEN"

        const val KEY_ROOM_TOKEN_PARAM = "KEY_ROOM_TOKEN_PARAM"
        const val KEY_ROOM_TOKEN_RESULT = "KEY_ROOM_TOKEN_RESULT"

        const val FOREGROUND_TOKEN_ID = 1003
    }
}