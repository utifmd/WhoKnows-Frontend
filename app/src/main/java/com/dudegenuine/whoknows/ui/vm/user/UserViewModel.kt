package com.dudegenuine.whoknows.ui.vm.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.User
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.compose.state.UserState
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel.Companion.USER_ID_SAVED_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

@HiltViewModel
class UserViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val fileCase: IFileUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IUserViewModel {
    private val TAG = javaClass.simpleName

    private val messagingToken = caseMessaging.onMessagingTokenized()
    private val _formState = mutableStateOf(UserState.FormState())
    val formState: UserState.FormState
        get() = _formState.value

    init {
        val navigated = savedStateHandle.get<String>(USER_ID_SAVED_KEY)

        if (navigated != null) navigated.let(this::getUser)
        else getUser()
    }

    private fun onRegisterGroupToken(currentUser: User) {
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }

        concatenate(joined, owned).forEach { roomId ->
            postTokenByNotifyKeyName(messagingToken, roomId)
        }
    }

    private fun onUnregisterGroupToken(currentUser: User) {
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId } //.also { Log.d(TAG, "onUnregisterGroupToken: ${it.joinToString(" -> ")}") }

        concatenate(joined, owned).forEach { roomId ->
            deleteTokenByNotifyKeyName(messagingToken, roomId)
        }
    }

    fun onUploadProfile() {
        val model: User? = state.user

        if (formState.profileImage.isEmpty() || model == null){
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        model.let { user ->
            fileCase.uploadFile(formState.profileImage).onEach { res ->
                onResourceSucceed(res) { file ->
                    val fresh = user.copy(profileUrl = file.url)

                    Log.d("onUploadProfile", fresh.toString())

                    patchUser(
                        id = fresh.id,
                        freshUser = fresh
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onUpdateUser(fieldKey: String?, fieldValue: String, onSucceed: (User) -> Unit) {

        if (fieldValue.isBlank() || state.user == null){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        state.user?.let { model ->
            val data = when(fieldKey) {

                IProfileEvent.NAME -> model.copy(
                    fullName = fieldValue)

                IProfileEvent.EMAIL -> model.copy(
                    email = fieldValue)

                IProfileEvent.PHONE -> model.copy(
                    phone = fieldValue)

                IProfileEvent.USERNAME -> model.copy(
                    username = fieldValue)

                IProfileEvent.PASSWORD -> model.copy(
                    password = fieldValue)

                IProfileEvent.URL_PROFILE -> model.copy(
                    profileUrl = fieldValue)

                else -> model
            }

            Log.d("ProfileEditScreen: ", "triggered")
            patchUser(data, onSucceed)
        }
    }

    private fun postTokenByNotifyKeyName(token: String, notifyKeyName: String){
        getMessagingGroupKey(notifyKeyName){ notifyKey ->
            val model = Messaging.GroupAdder(
                key = notifyKey,
                keyName = notifyKeyName,
                tokens = listOf(token)
            )

            addMessagingGroupMember(model){
                Log.d(TAG, "added: $it")
            }
        }
    }

    private fun deleteTokenByNotifyKeyName(token: String, notifyKeyName: String){
        getMessagingGroupKey(notifyKeyName){ notifyKey ->
            val model = Messaging.GroupRemover(
                key = notifyKey,
                keyName = notifyKeyName,
                tokens = listOf(token)
            )

            removeGroupMemberMessaging(model){
                Log.d(TAG, "removed: $it")
            }
        }
    }

    override fun signInUser() {

        val model = formState.loginModel
        if (!formState.isLoginValid.value){
            _authState.value = ResourceState.Auth(error = DONT_EMPTY)
            return
        }

        caseUser.signInUser(model)
            .onEach { res -> onAuth(res, ::onRegisterGroupToken) }//(this::onAuth)
            .launchIn(viewModelScope)
    }

    override fun signUpUser() {
        val model = formState.regisModel
        if (!formState.isRegisValid.value){
            _authState.value = ResourceState.Auth(error = DONT_EMPTY)
            return
        }

        caseUser.postUser(model)
            .onEach { res -> onAuth(res, ::onRegisterGroupToken) }
            .launchIn(viewModelScope)
    }

    override fun signOutUser() {
        getUser(caseUser.currentUserId()) { freshUser ->
            caseUser.signOutUser()
                .onEach { res -> onAuth(res, null) {
                    onUnregisterGroupToken(freshUser)

                    _authState.value = ResourceState.Auth()
                }}
                .launchIn(viewModelScope)
        }
    }

    override fun postUser(user: User) {
        if (user.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseUser.postUser(user)
            .onEach(::onResource).launchIn(viewModelScope)
    }

    override fun getUser() {
        caseUser.getUser()
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUser(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }
        caseUser.getUser(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUser(id: String, onSucceed: (User) -> Unit) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseUser.getUser(id)
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun patchUser(id: String, freshUser: User) {
        if (id.isBlank() || freshUser.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        freshUser.apply { updatedAt = Date() }

        caseUser.patchUser(id, freshUser)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchUser(freshUser: User, onSucceed: (User) -> Unit) {
        if (freshUser.id.isBlank() || freshUser.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        freshUser.apply { updatedAt = Date() }

        caseUser.patchUser(freshUser.id, freshUser)
            .onEach { onResourceSucceed(it, onSucceed) }.launchIn(viewModelScope)
    }

    override fun deleteUser(id: String) {
        if (id.isBlank()) {
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseUser.deleteUser(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUsers(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseUser.getUsers(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getMessagingGroupKey(
        keyName: String, onSucceed: (String) -> Unit) {
        if (keyName.isBlank()) return

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessagingGroupMember(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        if (!messaging.isValid) return

        caseMessaging.addMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun removeGroupMemberMessaging(
        messaging: Messaging.GroupRemover, onSucceed: (String) -> Unit) {
        if (!messaging.isValid) return

        caseMessaging.removeMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun pushMessaging(
        messaging: Messaging.Pusher, onSucceed: (String) -> Unit) {
        if (!messaging.isValid) return

        caseMessaging.pushMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }
}