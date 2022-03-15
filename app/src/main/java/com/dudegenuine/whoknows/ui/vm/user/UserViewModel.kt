package com.dudegenuine.whoknows.ui.vm.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.User
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.whoknows.infrastructure.common.Constants
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

@FlowPreview
@HiltViewModel
class UserViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val fileCase: IFileUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IUserViewModel {
    @Inject lateinit var share: IShareLauncher

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

    private fun onPreRegisterGroupToken(currentUser: User) {
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }

        concatenate(joined, owned).forEach { roomId ->
            onRegisterToken(messagingToken, roomId)
        }
    }

    private fun onPreUnregisterGroupToken(currentUser: User) {
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }

        concatenate(joined, owned).forEach { roomId ->
            onUnregisterToken(messagingToken, roomId)
        }
    }

    fun onUploadProfile() {
        val model: User? = state.user

        if (formState.profileImage.isEmpty() || model == null){
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        model.let { user ->
            fileCase.uploadFile(formState.profileImage)
                .onEach { res -> onResourceSucceed(res) { file ->
                        val fresh = user.copy(profileUrl = file.url)

                        Log.d("onUploadProfile", fresh.toString())
                        patchUser(id = fresh.id, freshUser = fresh)
                    }
                }
                .flatMapMerge { fileCase.deleteFile(user.profileUrl) }
                .launchIn(viewModelScope)
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

    private fun onRegisterToken(token: String, notifyKeyName: String){
        getMessaging(notifyKeyName){ notifyKey ->
            val model = Messaging.GroupAdder(
                key = notifyKey,
                keyName = notifyKeyName,
                tokens = listOf(token)
            )

            addMessaging(model) {
                Log.d(TAG, "added: $it")
            }
        }
    }

    private fun onUnregisterToken(token: String, notifyKeyName: String) {
        getMessaging(notifyKeyName) { notifyKey ->
            val model = Messaging.GroupRemover(notifyKeyName, listOf(token), notifyKey)

            removeMessaging(model) { Log.d(TAG, "removed: $it") }
        }
    }

    fun onSharePressed(userId: String){
        val data = "${Constants.BASE_CLIENT_URL}/who-knows/user/$userId"
        share.launch(data)
    }

    override fun signInUser() {
        val model = formState.loginModel
        if (!formState.isLoginValid.value){
            _authState.value = ResourceState.Auth(error = DONT_EMPTY)
            return
        }

        caseUser.signInUser(model)
            .onEach { res -> onAuth(res, ::onPreRegisterGroupToken) } //(this::onAuth)
            .launchIn(viewModelScope)
    }

    override fun signUpUser() {
        val model = formState.regisModel
        if (!formState.isRegisValid.value){
            _authState.value = ResourceState.Auth(error = DONT_EMPTY)
            return
        }

        caseUser.postUser(model)
            .onEach { res -> onAuth(res, ::onPreRegisterGroupToken) }
            .launchIn(viewModelScope)
    }

    override fun signOutUser() {
        getUser(caseUser.currentUserId()) { freshUser ->
            caseUser.signOutUser()
                .onEach { res -> onAuth(res, null) {
                    onPreUnregisterGroupToken(freshUser)

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

    override fun getMessaging(
        keyName: String, onSucceed: (String) -> Unit) {
        if (keyName.isBlank()) return

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessaging(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        if (!messaging.isValid) return

        caseMessaging.addMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun removeMessaging(
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