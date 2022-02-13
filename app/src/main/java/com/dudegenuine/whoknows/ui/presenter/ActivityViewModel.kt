package com.dudegenuine.whoknows.ui.presenter

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class ActivityViewModel: BaseViewModel() {
    private val TAG: String = javaClass.simpleName
    private var messaging: FirebaseMessaging = FirebaseMessaging.getInstance()

    companion object {
        const val TOPIC_COMMON = "/topics/common"
    }

    init {
        tokenizeFcmToken()

        subscribeTopic()
    }

    private fun subscribeTopic() {
        messaging.subscribeToTopic(TOPIC_COMMON)
    }

    private fun tokenizeFcmToken(){
        messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@addOnCompleteListener
            }

            val token = task.result

            Log.d("tokenizeFcmToken: ", token)
        }
    }

    fun onStateValueChange(state: ResourceState) {
        _state.value = state
    }
}