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
import com.dudegenuine.model.Resource
import com.dudegenuine.model.User
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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
        /*when {
            prefsFactory.createMessaging.isNotBlank()  -> {
                Log.d(TAG, "retryFailureAttempted: create ${prefsFactory.createMessaging}")
                onCreateMessaging()
            }
            prefsFactory.addMessaging -> onPreRegisterMessaging {
                Log.d(TAG, "retryFailureAttempted: add ${it.username}")
            }
            prefsFactory.removeMessaging -> onPreUnregisterMessaging {
                Log.d(TAG, "retryFailureAttempted: remove ${it.username}")
            }
        }*/
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
            onPreRegisterMessaging { Log.d(TAG, "messagingServiceReceiver: onPreRegisterMessaging") }
    }

    private fun messagingSubscribeTopic() {
        messaging.apply {
            isAutoInitEnabled = true
            subscribeToTopic(TOPIC_COMMON)
        }
    }

    private fun messagingInitToken() {
        retryFailureAttempted()
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

    private fun onPreRegisterMessaging(onFinished: (User.Complete) -> Unit){
        caseUser.getUser(userId)
            .flatMapConcat { res -> onResourceAuthFlow(res) { currentUser ->
                val joined = currentUser.participants.map { it.roomId }
                val owned = currentUser.rooms.map { it.roomId }

                concatenate(joined, owned).asFlow()
                    .flatMapConcat(::onRegisterMessaging)
                    .mapLatest { Resource.Success(currentUser) }}}

            .onEach { res -> onResourceStateless(res, onFinished) }
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(viewModelScope)
    }

    private fun onPreUnregisterMessaging(onFinished: (User.Complete) -> Unit){
        caseUser.getUser(userId)
            .flatMapConcat { res -> onResourceAuthFlow(res) { currentUser ->
                val joined = currentUser.participants.map { it.roomId }
                val owned = currentUser.rooms.map { it.roomId }

                concatenate(joined, owned).asFlow()
                    .flatMapConcat(::onUnregisterMessaging)
                    .mapLatest { Resource.Success(currentUser) }}}

            .onEach { res -> onResourceStateless(res, onFinished) }
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(viewModelScope)
    }

    private fun onRegisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                caseMessaging.addMessaging(
                    Messaging.GroupAdder(roomId, listOf(prefsFactory.tokenId), key)) }}
            .onEach { onResourceStateless(it) { onRejectAddMessaging() } }

    private fun onUnregisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                caseMessaging.removeMessaging(
                    Messaging.GroupRemover(roomId, listOf(prefsFactory.tokenId), key)) }}
            .onEach { onResourceStateless(it) { onRejectRemoveMessaging() } }

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
        }

        /*prefs?.all?.forEach {
            Log.d(TAG, "${it.key}: ${it.value}")
        }*/
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
            .onEach { res -> onResourceStateless(res) { onRejectCreateMessaging() }}
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