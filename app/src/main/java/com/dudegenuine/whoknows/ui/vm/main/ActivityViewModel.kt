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
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IPrefsFactory.Companion.NOTIFICATION_BADGE
import com.dudegenuine.local.api.IPrefsFactory.Companion.USER_ID
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
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
    private val prefsFactory: IPrefsFactory,
    private val caseMessaging: IMessageUseCaseModule,
    private val caseNotifier: INotificationUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val savedStateHandle: SavedStateHandle): IActivityViewModel() {
    companion object { const val TOPIC_COMMON = "common" }

    private val TAG: String = javaClass.simpleName
    private val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()

    val userId get() = prefsFactory.userId
    var isSignedIn by mutableStateOf(userId.isNotBlank())
    val badge get() = prefsFactory.notificationBadge

    init {
        messagingSubscribeTopic()

        if (prefsFactory.tokenId.isBlank()) messagingInitToken()
        else Log.d(TAG, "currentToken: ${prefsFactory.tokenId}")

        getNotifications(prefsFactory.userId)
    }

    val networkServiceAction = IntentFilter(IReceiverFactory.ACTION_CONNECTIVITY_CHANGE)
    val networkServiceReceiver = caseMessaging.onInternetReceived { message ->
        if (message.isNotBlank()) if(prefsFactory.tokenId.isBlank()) messagingInitToken()

        onShowSnackBar(message)
    }

    val messagingServiceAction = IntentFilter(IReceiverFactory.ACTION_FCM_TOKEN)
    val messagingServiceReceiver = caseMessaging.onTokenReceived { token ->
        token.apply (::onTokenIdChange)

        if (prefsFactory.userId.isNotBlank()) onPreRegisterToken(token)
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
                Log.d(TAG, "Success: $it")
                onTokenIdChange(it)
            }
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

    private fun getJoinedOwnedRoomIds(onSucceed: (List<String>) -> Unit) {
        if (prefsFactory.userId.isBlank()) return

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
                val register = Messaging.GroupAdder(roomId, listOf(prefsFactory.tokenId), key)
                caseMessaging.addMessaging(register)
            }
        }
    }

    fun registerPrefsListener() { prefsFactory.manager.register(onPrefsListener) }
    fun unregisterPrefsListener() { prefsFactory.manager.unregister(onPrefsListener) }

    private val onPrefsListener = SharedPreferences
        .OnSharedPreferenceChangeListener { prefs, key ->

        when (key) {
            USER_ID -> {
                val fresh = prefs?.getString(USER_ID, "")

                fresh?.let(::onUserIdChange)
            }
            NOTIFICATION_BADGE -> {
                val preBadge = prefs?.getInt(NOTIFICATION_BADGE, 0)
                val fresh = preBadge ?: 0

                onBadgeChange(fresh)
            }
        }

        prefs?.all?.forEach {
            Log.d(TAG, "${it.key}: ${it.value}")
        }
    }

    private fun getNotifications(userId: String){
        if (userId.isBlank()) return

        caseNotifier.getNotifications(userId, 0, Int.MAX_VALUE)
            .onEach { res -> onResourceStateless(res) { notifiers ->
                val counter = notifiers.count { !it.seen }
                onBadgeChange(counter)
            }}
            .launchIn(viewModelScope)
    }

    /*fun onTurnNotifierOn(fresh: Boolean){
        isNotify = fresh

        caseMessaging.onBadgeStatusRefresh(fresh)
    }*/

    private fun onBadgeChange(fresh: Int) {
        prefsFactory.notificationBadge = fresh
    }

    private fun onUserIdChange(fresh: String) {
        isSignedIn = fresh.isNotBlank()

        prefsFactory.userId = fresh
    }

    private fun onTokenIdChange(fresh: String) {
        prefsFactory.tokenId = fresh
    }
}