package com.dudegenuine.whoknows.ui.vm.notification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Notification
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.ui.compose.state.NotificationState
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.notification.contract.INotificationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
class NotificationViewModel
    @Inject constructor(
    private val case: INotificationUseCaseModule,
    private val savedState: SavedStateHandle): BaseViewModel(), INotificationViewModel {
    private val TAG = javaClass.simpleName

    private val _formState = mutableStateOf(NotificationState.FormState())
    val formState = _formState.value

    init {
        Log.d(TAG, "currentUserId: ${case.currentUserId()}")

        getNotifications(case.currentUserId(), 0, 15)
    }

    val onStateValueChange: (ResourceState) -> Unit = { _state.value = it }

    override fun postNotification(notification: Notification) {
        if (notification.isPropsBlank) {

            onStateValueChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.postNotification(notification)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchNotification(notification: Notification) {
        if (notification.isPropsBlank) {
            onStateValueChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.patchNotification(notification)
            .onEach { res -> onResourceStateless(res)
                { Log.d(TAG, "patchNotification: ${it.notificationId}") }}
            .launchIn(viewModelScope)
    }

    override fun getNotification(id: String) {
        if (id.isBlank()){

            onStateValueChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.getNotification(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteNotification(id: String) {
        if (id.isBlank()){

            onStateValueChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.deleteNotification(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getNotifications(page: Int, size: Int) {
        if (size <= 0){

            onStateValueChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.getNotifications(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getNotifications(recipientId: String, page: Int, size: Int) {
        if (size <= 0){

            onStateValueChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.getNotifications(recipientId, page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}