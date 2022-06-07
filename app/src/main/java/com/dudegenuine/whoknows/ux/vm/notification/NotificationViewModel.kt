package com.dudegenuine.whoknows.ux.vm.notification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Notification
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.compose.state.NotificationState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import com.dudegenuine.whoknows.ux.vm.notification.contract.INotificationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class)
class NotificationViewModel
    @Inject constructor(
        private val prefsFactory: IPrefsFactory,
        private val caseNotify: INotificationUseCaseModule,
        private val caseUser: IUserUseCaseModule,
        private val savedState: SavedStateHandle): BaseViewModel(), INotificationViewModel {
    private val TAG = javaClass.simpleName
    //private var badge by mutableStateOf(prefsFactory.notificationBadge)
    val currentUserId get() = prefsFactory.userId

    private val _formState = mutableStateOf(NotificationState.FormState())
    val formState = _formState.value

    init { getNotifications() }

    fun getNotifications() {
        if(prefsFactory.userId.isNotBlank())
            getNotifications(prefsFactory.userId, 0, Int.MAX_VALUE)
    }

    fun onReadNotification(notification: Notification, onStart: () -> Unit) {
        if (!notification.seen) caseNotify.patchNotification(notification.copy(seen = true))
            .onStart {
                onStateChange(ResourceState(
                    badge = state.badge -1))
                onStart()
            }
            .onCompletion { cause ->
                if (cause == null) onStateChange(ResourceState(
                    notifications = state.notifications?.filter { it != notification }))
            }
            .launchIn(viewModelScope)
    }

    override fun postNotification(notification: Notification) {
        if (notification.isPropsBlank) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.postNotification(notification)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchNotification(
        notification: Notification, onSuccess: (Notification) -> Unit) {

        if (notification.isPropsBlank) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.patchNotification(notification)
            .onEach { res -> onResourceStateless(res, onSuccess) }
            .launchIn(viewModelScope)
    }

    override fun getNotification(id: String){
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.getNotification(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteNotification(id: String){
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.deleteNotification(id)
            .flatMapMerge { caseNotify.getNotifications(prefsFactory.userId, 0, Int.MAX_VALUE) }
            .onEach(::onResource)
            .catch { Log.d(TAG, "deleteNotification->cause: ${it.localizedMessage}") }
            .launchIn(viewModelScope)

            /*.onEach { res ->
                if(res is Resource.Success) // onStateChange(state.copy(notifications = state.notifications?.filter { it.notificationId != id }))
                    getNotifications()
            }*/
    }

    override fun getNotifications(page: Int, size: Int){
        if (size <= 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.getNotifications(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getNotifications(recipientId: String, page: Int, size: Int){
        if (size < 1){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.getNotifications(recipientId, page, size)
            .onEach(::onResource).launchIn(viewModelScope)
            /*.onEach { res ->
                if (res is Resource.ScaseNotify.getNotifications(recipientId, page, size)
            .onEach(::onResource)uccess){
                    val badges = res.data?.count { !it.seen } ?: 0
                    caseNotify.onChangeCurrentBadge(badges)
                }
            }*/
    }

    /*private fun onBadgeChange(fresh: Int){
        prefsFactory.notificationBadge = fresh
    }*/
}