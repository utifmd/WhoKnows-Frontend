package com.dudegenuine.repository.contract.dependency.remote

import com.google.firebase.messaging.FirebaseMessaging

/**
 * Sun, 29 May 2022
 * WhoKnows by utifmd
 **/
interface IFirebaseManager {
    val messaging: () -> FirebaseMessaging

    companion object {
        const val TAG = "IFirebaseManager"
        const val TOPIC_COMMON = "common"
    }
}