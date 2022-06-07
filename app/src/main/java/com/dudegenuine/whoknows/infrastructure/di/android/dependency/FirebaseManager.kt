package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Sun, 29 May 2022
 * WhoKnows by utifmd
 **/
class FirebaseManager: IFirebaseManager {
    override val messaging: () ->
        FirebaseMessaging = FirebaseMessaging::getInstance
}