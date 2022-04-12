package com.dudegenuine.whoknows.ui.vm.main

import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IPrefsFactory.Companion.NOTIFICATION_BADGE
import com.dudegenuine.local.api.IPrefsFactory.Companion.USER_ID
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.User
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
@HiltViewModel
@OptIn(FlowPreview::class)
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
    var badge by mutableStateOf(prefsFactory.notificationBadge)
    var isSignedIn by mutableStateOf(userId.isNotBlank())

    init {
        messagingSubscribeTopic()

        if (prefsFactory.tokenId.isBlank()) messagingInitToken()
        else Log.d(TAG, "currentToken: ${prefsFactory.tokenId}")

        getNotifications(prefsFactory.userId)
        retryFailureAttempted()
    }

    private fun retryFailureAttempted() {
        // TODO: check failure state in prefs then retry calls each functions everytime
        // 1. login messaging
        // 1. logout messaging
        // 1. create room messaging
        // 1. boarding messaging

        when {
            prefsFactory.createMessaging.isNotBlank()  -> {
                Log.d(TAG, "retryFailureAttempted: create ${prefsFactory.createMessaging}")
                onCreateMessaging()
            }
            prefsFactory.addMessaging  -> {
                Log.d(TAG, "retryFailureAttempted: add ${prefsFactory.addMessaging}")
                getUser(::onPreRegisterMessaging)
            }
            prefsFactory.removeMessaging  -> {
                Log.d(TAG, "retryFailureAttempted: remove ${prefsFactory.removeMessaging}")
                getUser(::onPreUnregisterMessaging)
            }
        }
    }

    val networkServiceAction = IntentFilter(IReceiverFactory.ACTION_CONNECTIVITY_CHANGE)
    val networkServiceReceiver = caseMessaging.onInternetReceived { message ->
        if (message.isNotBlank()) if(prefsFactory.tokenId.isBlank()) messagingInitToken()

        onShowSnackBar(message)
    }

    val messagingServiceAction = IntentFilter(IReceiverFactory.ACTION_FCM_TOKEN)
    val messagingServiceReceiver = caseMessaging.onTokenReceived { token ->
        Log.d(TAG, "messagingServiceReceiver: triggered")

        token.apply (::onTokenIdChange)
        if (prefsFactory.userId.isNotBlank())
            getUser(::onPreRegisterMessaging)
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


    private fun onPreRegisterMessaging(currentUser: User.Complete) {
        val TAG = object{}.javaClass.enclosingMethod?.name
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }
        val unread = currentUser.notifications.count { !it.seen }

        concatenate(joined, owned).asFlow()
            .flatMapConcat(::onRegisterMessaging)
            .onStart { onBadgeChange(unread) }
            .onCompletion { it?.let { onFlowFailed(TAG, it) } ?: onRejectAddMessaging() }
            .catch { onFlowFailed(TAG, it) }
            .launchIn(viewModelScope)
    }

    private fun onPreUnregisterMessaging(currentUser: User.Complete) {
        val TAG = object{}.javaClass.enclosingMethod?.name
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }

        concatenate(joined, owned).asFlow()
            .flatMapConcat(::onUnregisterMessaging)
            .onStart { onBadgeChange(0) }
            .onCompletion { it?.let { onFlowFailed(TAG, it) } ?: onRejectRemoveMessaging() }
            .catch { onFlowFailed(TAG, it) }
            .launchIn(viewModelScope)
    }

    private fun onRegisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                val register = Messaging.GroupAdder(roomId, listOf(prefsFactory.tokenId), key)
                caseMessaging.addMessaging(register)
            }}

    private fun onUnregisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                val remover = Messaging.GroupRemover(roomId, listOf(prefsFactory.tokenId), key)
                caseMessaging.removeMessaging(remover)
            }}

    fun registerPrefsListener() { prefsFactory.manager.register(onPrefsListener) }
    fun unregisterPrefsListener() { prefsFactory.manager.unregister(onPrefsListener) }

    private val onPrefsListener = SharedPreferences
        .OnSharedPreferenceChangeListener { prefs, key -> when (key) {
            USER_ID -> {
                val fresh = prefs?.getString(USER_ID, "")

                fresh?.let(::onUserIdChange)
            }
            NOTIFICATION_BADGE -> {
                val fresh = prefs?.getInt(NOTIFICATION_BADGE, 0)

                fresh?.let(::onBadgeChange)
            }
        } /*prefs?.all?.forEach { Log.d(TAG, "${it.key}: ${it.value}") }*/
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

    private fun getUser(onSucceed: (User.Complete) -> Unit){
        caseUser.getUser(userId)
            .onEach { onResourceSucceed(it, onSucceed) }
            .launchIn(viewModelScope)
    }

    private fun onCreateMessaging() {
        val create = Messaging.GroupCreator(prefsFactory.createMessaging, listOf(prefsFactory.tokenId))
        caseMessaging.createMessaging(create)
            .onCompletion { if(it == null) onRejectCreateMessaging() }
            .launchIn(viewModelScope)
    }

    private fun onBadgeChange(fresh: Int) {
        badge = fresh
        prefsFactory.notificationBadge = fresh
    }

    private fun onUserIdChange(fresh: String) {
        isSignedIn = fresh.isNotBlank()
        prefsFactory.userId = fresh
    }

    private fun onTokenIdChange(fresh: String) {
        prefsFactory.tokenId = fresh
    }

    private fun onRejectCreateMessaging() {
        prefsFactory.addMessaging = false
    }

    private fun onRejectAddMessaging() {
        prefsFactory.addMessaging = false
    }

    private fun onRejectRemoveMessaging() {
        prefsFactory.removeMessaging = false
    }
}