package com.dudegenuine.whoknows.ui.vm.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Messaging
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.service.MessagingService
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@HiltViewModel
class ActivityViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IActivityViewModel {
    private val TAG: String = javaClass.simpleName
    private var messaging: FirebaseMessaging = FirebaseMessaging.getInstance()
    private val isSignedIn = caseUser.currentUserId().isNotBlank()

    companion object {
        const val TOPIC_COMMON = "common"
    }

    init {
        //fcmTopicSubscribe()
        //if (isSignedIn) fcmTokenInstantiate()

        getMessagingGroupKey("anyone-knows-room-soekarno"){
            Log.d(TAG, "init: $it")
        }
    }

    val fcmTokenAction = IntentFilter(MessagingService.ACTION_FCM_TOKEN)

    val fcmTokenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val token = intent?.getStringExtra(MessagingService.INITIAL_FCM_TOKEN) ?: return

            if (isSignedIn) token.apply(::onRefreshToken)
            Log.d(TAG, "onReceive: $token")
        }
    }

    private fun fcmTopicSubscribe() {
        messaging.subscribeToTopic(TOPIC_COMMON)
    }

    private fun fcmTokenInstantiate(){
        messaging.token.addOnCompleteListener { task ->
            Log.d(TAG, "fcmTokenInstantiate: triggered")
            if (!task.isSuccessful) return@addOnCompleteListener
            val token = task.result

            // TODO: save value to prefs prepare for join selection room
            /*getJoinedRoomIds { ids -> ids
                .forEach { addTokenByNotifyKeyName(token, it) }

                Log.d(TAG, "getJoinedRoomIds: ${ids.joinToString(", ")}")
            }*/
        }
    }

    private fun onRefreshToken(token: String) {
        Log.d(TAG, "onRefreshToken: $token")

        getJoinedRoomIds { ids -> ids
            .forEach { addTokenByNotifyKeyName(token, it) }

            Log.d(TAG, "getJoinedRoomIds: ${ids.joinToString(", ")}")
        }
    }

    override fun getMessagingGroupKey(keyName: String, onSucceed: (String) -> Unit) {
        if (keyName.isBlank()) _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun getJoinedRoomIds(onSucceed: (List<String>) -> Unit) {
        caseUser.getUser(caseUser.currentUserId())
            .onEach { res -> onResourceSucceed(res) { usr ->
                val participateIds = usr.participants.map { it.roomId }

                onSucceed(participateIds)}}
            .launchIn(viewModelScope)
    }

    override fun addMessagingGroupMember(messaging: Messaging.GroupAdder) {
        val model = messaging.copy()

        if (model.keyName.isBlank() or model.key.isBlank() or model.tokens.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.createMessaging(model)
            .onEach(::onResource).launchIn(viewModelScope)
    }

    private fun addTokenByNotifyKeyName(token: String, roomId: String){
        getMessagingGroupKey(roomId){ notifyKey ->
            val model = Messaging.GroupAdder(
                key = notifyKey,
                keyName = roomId,
                tokens = listOf(token)
            )

            addMessagingGroupMember(model)
        }
    }

    fun onStateValueChange(state: ResourceState) {
        _state.value = state
    }
}