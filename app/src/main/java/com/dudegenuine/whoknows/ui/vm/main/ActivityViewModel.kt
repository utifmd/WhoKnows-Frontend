package com.dudegenuine.whoknows.ui.vm.main

import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_NOTIFICATION_BADGE
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class ActivityViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseNotifier: INotificationUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IActivityViewModel {
    companion object { const val TOPIC_COMMON = "common" }

    private val TAG: String = javaClass.simpleName
    private val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()
    private val currentUserId = caseUser.currentUserId()

    var token by mutableStateOf(caseMessaging.currentToken())
    var badge by mutableStateOf(caseUser.currentBadge())
    var isNotify by mutableStateOf(caseMessaging.currentBadgeStatus())

    init {
        messagingSubscribeTopic()

        if (token.isBlank()) messagingInitToken()
        else Log.d(TAG, "currentToken: $token")

        getNotifications(currentUserId)
    }

    val networkServiceAction = IntentFilter(IReceiverFactory.ACTION_CONNECTIVITY_CHANGE)
    val networkServiceReceiver = caseMessaging.onInternetReceived { message ->
        if (message.isNotBlank()) if(token.isBlank()) messagingInitToken()

        onShowSnackBar(message)
    }
    val messagingServiceAction = IntentFilter(IReceiverFactory.ACTION_FCM_TOKEN)
    val messagingServiceReceiver = caseMessaging.onTokenReceived { token ->
        token.apply (::onMessagingTokenChange)

        if (currentUserId.isNotBlank()) onPreRegisterToken(token)
    }

    private fun onMessagingTokenChange(fresh: String){
        token = fresh
        caseMessaging.onTokenRefresh(fresh) //MESSAGING_TOKEN
    }

    private fun messagingSubscribeTopic() {
        messaging.apply {
            isAutoInitEnabled = true
            subscribeToTopic(TOPIC_COMMON)
        }
    }

    private fun messagingInitToken() {
        with (messaging.token) {
            addOnSuccessListener {
                Log.d(TAG, "Success: $it") }
            addOnCanceledListener {
                Log.d(TAG, "Canceled: triggered") }
            addOnFailureListener {
                Log.d(TAG, "Failure: ${it.localizedMessage}")
            }
        }
    }

    private fun onPreRegisterToken(token: String) {
        Log.d(TAG, "onPreRegisterToken: $token")

        getJoinedOwnedRoomIds { roomIds ->
            roomIds.asFlow()
                .flatMapConcat(::onRegisterMessaging)
                .launchIn(viewModelScope)
        }
    }

    override fun getJoinedOwnedRoomIds(onSucceed: (List<String>) -> Unit) {
        if (currentUserId.isBlank()) return

        caseUser.getUser()
            .onEach { res -> onResourceSucceed(res) { usr ->
                val joined = usr.participants.map { it.roomId }
                val owned = usr.rooms.map { it.roomId }

                onSucceed(concatenate(owned, joined)) }}
            .launchIn(viewModelScope)
    }


    private fun onRegisterMessaging(roomId: String): Flow<Resource<out Any>> {
        return caseMessaging.getMessaging(roomId)
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                val register = Messaging.GroupAdder(roomId, listOf(token), key)
                caseMessaging.addMessaging(register)
            }
        }
    }

    fun registerPrefsListener() { caseUser.registerPrefsListener(onPrefsListener) }
    fun unregisterPrefsListener() { caseUser.unregisterPrefsListener(onPrefsListener) }

    private val onPrefsListener = SharedPreferences
        .OnSharedPreferenceChangeListener { prefs, key ->

        if (key == CURRENT_NOTIFICATION_BADGE) {
            val preBadge = prefs?.getInt(CURRENT_NOTIFICATION_BADGE, 0)
            val fresh = preBadge ?: 0

            onBadgeChange(fresh)
        }
    }

    private fun getNotifications(userId: String){
        if (userId.isBlank()) return

        caseNotifier.getNotifications(userId, 0, Int.MAX_VALUE)
            .onEach { res -> onResourceStateless(res) { notifiers ->
                val counter = notifiers.count { !it.seen } //Log.d(TAG, "counter: $counter badge: $badge")

                if (counter > 0) onTurnNotifierOn(true)
                caseUser.onChangeCurrentBadge(counter) }} //onBadgeChange(counter)
            .launchIn(viewModelScope)
    }

    fun onTurnNotifierOn(fresh: Boolean){
        isNotify = fresh

        caseMessaging.onBadgeStatusRefresh(fresh)
    }

    private fun onBadgeChange(fresh: Int){ badge = fresh }
}