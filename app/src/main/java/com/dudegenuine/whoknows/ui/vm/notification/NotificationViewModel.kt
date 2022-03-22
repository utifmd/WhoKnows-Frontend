package com.dudegenuine.whoknows.ui.vm.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Notification
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
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
    private val caseNotify: INotificationUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val savedState: SavedStateHandle): BaseViewModel(), INotificationViewModel {
    private val TAG = javaClass.simpleName
    private val currentUserId = caseNotify.currentUserId()
    private val currentBadge = caseNotify.currentBadge()

    private val _formState = mutableStateOf(NotificationState.FormState())
    val formState = _formState.value

    init { getNotifications(currentUserId, 0, Int.MAX_VALUE) }

    private var badge by mutableStateOf(currentBadge)

    /*fun onNotifierIncrease() {
        badge -= 1

        caseNotify.onChangeCurrentBadge(badge)
        Log.d(TAG, "onNotifierIncrease: $badge")
    }*/

    fun onNotificationDecrease(notification: Notification) {
        val model = notification.copy(seen = true)
        badge -= 1

        patchNotification(model) {
            caseNotify.onChangeCurrentBadge(badge)
        }
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

    override fun getNotification(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.getNotification(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteNotification(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.deleteNotification(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getNotifications(page: Int, size: Int) {
        if (size <= 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.getNotifications(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getNotifications(recipientId: String, page: Int, size: Int) {
        if (size < 1){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseNotify.getNotifications(recipientId, page, size)
            .onEach(::onResource).launchIn(viewModelScope)
            /*.onEach { res ->
                if (res is Resource.Success){
                    val badges = res.data?.count { !it.seen } ?: 0
                    caseNotify.onChangeCurrentBadge(badges)
                }
            }*/
    }
}