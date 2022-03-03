package com.dudegenuine.whoknows.ui.vm.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Messaging
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.service.MessagingService
import com.dudegenuine.whoknows.ui.service.MessagingService.Companion.INITIAL_FCM_TOKEN
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
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class ActivityViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IActivityViewModel {
    private val TAG: String = javaClass.simpleName
    private val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()

    private val messagingToken = caseMessaging.onMessagingTokenized() //private val isSignedIn = caseUser.currentUserId().isNotBlank()
    private val isTokenized = messagingToken.isNotBlank()

    companion object {
        const val TOPIC_COMMON = "common" //"/topics/common"
    }

    init {
        messagingSubscribeTopic()

        if (!isTokenized) messagingInitToken()
        else Log.d(TAG, "local store token: $messagingToken")
    }

    private fun messagingSubscribeTopic() {
        messaging.apply {
            isAutoInitEnabled = true
            subscribeToTopic(TOPIC_COMMON)
        }
    }

    private fun messagingInitToken(){
        messaging.token.addOnCompleteListener { task ->
            Log.d(TAG, "messagingInitToken: triggered")

            if (!task.isSuccessful) {
                Log.d(TAG, "messagingInitToken: ${task.exception?.localizedMessage}")

                return@addOnCompleteListener
            }

            val token = task.result
            // TODO: save value to prefs prepare for join selection room
            caseMessaging.onMessagingTokenRefresh(token)
        }
    }

    private fun onRefreshToken(token: String) {
        Log.d(TAG, "onRefreshToken: $token")

        getJoinedRoomIds { roomIds -> roomIds
            .forEach { addTokenByNotifyKeyName(token, it) }

            Log.d(TAG, "getJoinedRoomIds: ${roomIds.joinToString(" ~> ")}")
        }
    }

    override fun getJoinedRoomIds(onSucceed: (List<String>) -> Unit) {
        caseUser.getUser(caseUser.currentUserId())
            .onEach { res -> onResourceSucceed(res) { usr ->
                val participateIds = usr.participants.map { it.roomId }

                onSucceed(participateIds)}}
            .launchIn(viewModelScope)
    }

    private fun addTokenByNotifyKeyName(token: String, roomId: String){
        getMessagingGroupKey(roomId){ notifyKey ->
            val model = Messaging.GroupAdder(
                key = notifyKey,
                keyName = roomId,
                tokens = listOf(token)
            )

            addMessagingGroupMember(model){
                Log.d(TAG, "addTokenByNotifyKeyName: $it")
            }
        }
    }

    val messagingServiceAction = IntentFilter(MessagingService.ACTION_FCM_TOKEN)
    val messagingServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val token = intent?.getStringExtra(INITIAL_FCM_TOKEN) ?: return
            /*if (isSignedIn) */
            token.let {
                caseMessaging.onMessagingTokenRefresh(it)

                ::onRefreshToken
            }
            Log.d(TAG, "onReceive: triggered")
        }
    }

    override fun getMessagingGroupKey(keyName: String, onSucceed: (String) -> Unit) {
        if (keyName.isBlank()) _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessagingGroupMember(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.keyName.isBlank() or model.key.isBlank() or model.tokens.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.createMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    fun onStateValueChange(state: ResourceState) {
        _state.value = state
    }
}