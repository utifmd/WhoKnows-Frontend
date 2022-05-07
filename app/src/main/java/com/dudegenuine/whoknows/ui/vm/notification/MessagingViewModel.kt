package com.dudegenuine.whoknows.ui.vm.notification

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.notification.contract.IMessagingViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Sun, 24 Apr 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class)
class MessagingViewModel
    @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val caseMessaging: IMessageUseCaseModule,
    private val prefsFactory: IPrefsFactory): IMessagingViewModel, BaseViewModel() {
    private val TAG: String = javaClass.simpleName

    fun onRemoveMessaging(args: String) {
        val elem = JsonParser.parseString(args)
        if (elem !is JsonObject) return
        if (!elem.has("roomId")) return
        if (!elem.has("userId")) return
        val roomId = elem["roomId"].asString
        val userId = elem["userId"].asString

        if (userId != prefsFactory.userId) return
        caseMessaging.getMessaging(roomId)
            .flatMapConcat { res -> when(res){
                is Resource.Success -> res.data?.let { notification_key ->
                    val remover = Messaging.GroupRemover(roomId, listOf(prefsFactory.tokenId), notification_key)
                    caseMessaging.removeMessaging(remover)

                } ?: emptyFlow()
                else -> emptyFlow() }
            }
            .onEach {
                Log.d(TAG, "onRemoveMessaging: ${it.data}")
            }
            .launchIn(viewModelScope)

        Log.d(TAG, "onRemoveMessaging: $roomId")
    }

    fun onTokenIdChange(token: String) {
        prefsFactory.tokenId = token
    }

    fun onBadgeChange(fresh: Int) {
        prefsFactory.notificationBadge = fresh
    }
}