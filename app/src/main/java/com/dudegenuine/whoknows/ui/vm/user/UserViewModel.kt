package com.dudegenuine.whoknows.ui.vm.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.model.BuildConfig
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.User
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.compose.state.UserState
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.CHECK_CONN
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class)
class UserViewModel
    @Inject constructor(
    private val prefsFactory: IPrefsFactory,
    private val caseMessaging: IMessageUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val fileCase: IFileUseCaseModule,
    private val savedStateHandle: SavedStateHandle) : IUserViewModel() {
    private val TAG = javaClass.simpleName
    @Inject
    lateinit var notifier: INotifyManager
    @Inject
    lateinit var share: IShareLauncher

    private val _formState = mutableStateOf(UserState.FormState())
    val formState: UserState.FormState
        get() = _formState.value

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    init {
        val navigated = savedStateHandle.get<String>(USER_ID_SAVED_KEY)

        navigated?.let(this::getUser) ?: getUser()
        if (prefsFactory.userId.isNotBlank()) getUser()
    }

    fun onSharePressed(userId: String) {
        val data = "${BuildConfig.BASE_CLIENT_URL}/who-knows/user/$userId"
        share.launch(data)
    }

    private fun onPreRegisterMessaging(currentUser: User.Complete) {
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }
        val unread = currentUser.notifications.count { !it.seen }

        concatenate(joined, owned)
            .map(::onRegisterMessaging)
            .asFlow()
            .onStart { onBadgeChange(unread) }
            .flattenMerge()
            .catch { cause -> /*cause is IOException then save in prefs*/
                Log.d(TAG, "onPreRegisterGroupToken->catch: ${cause.localizedMessage}") }
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(scope)
    }

    private fun onPreUnregisterMessaging(currentUser: User.Complete) {
        val joined = currentUser.participants.map { it.roomId }
        val owned = currentUser.rooms.map { it.roomId }

        concatenate(joined, owned)
            .map(::onUnregisterMessaging)
            .asFlow()
            .onStart { onBadgeChange(0) }
            .flattenMerge()
            .catch { cause -> /*cause is IOException then save in prefs*/
                Log.d(TAG, "onPreUnregisterGroupToken->catch: ${cause.localizedMessage}") }
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(scope)
    }

    private fun onRegisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .flatMapConcat { res ->
                onResourceFlow(res) { key ->
                    val register = Messaging.GroupAdder(roomId, listOf(prefsFactory.tokenId), key)
                    caseMessaging.addMessaging(register)
                }
            }

    private fun onUnregisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .flatMapConcat { res ->
                onResourceFlow(res) { key ->
                    val remover = Messaging.GroupRemover(roomId, listOf(prefsFactory.tokenId), key)
                    caseMessaging.removeMessaging(remover)
                }
            }

    override fun signUpUser() {
        val model = formState.regisModel
        if (!formState.isRegisValid.value) {
            onAuthStateChange(ResourceState.Auth(error = DONT_EMPTY))
            return
        }

        if (prefsFactory.tokenId.isBlank()) {
            onAuthStateChange(ResourceState.Auth(error = CHECK_CONN))
            return
        }

        caseUser.postUser(model)
            .onEach { onAuth(it, ::onPreRegisterMessaging) }
            .launchIn(viewModelScope)
    }

    override fun signInUser() {
        val model = formState.loginModel
        if (!formState.isLoginValid.value) {
            onAuthStateChange(ResourceState.Auth(error = DONT_EMPTY))
            return
        }

        if (prefsFactory.tokenId.isBlank()) {
            onAuthStateChange(ResourceState.Auth(error = CHECK_CONN))
            return
        }

        caseUser.signInUser(model)
            .onEach { onAuth(it, ::onPreRegisterMessaging) }
            .launchIn(scope)
    }

    override fun signOutUser() {
        val userId = prefsFactory.userId
        if (prefsFactory.tokenId.isBlank() or userId.isBlank()) {
            onAuthStateChange(ResourceState.Auth(error = CHECK_CONN))
            return
        }

        caseUser.getUser(userId)
            .onEach { onAuth(it, ::onPreUnregisterMessaging) }
            .flatMapMerge { caseUser.signOutUser() }
            .onEach { onAuth(it, onSignedOut = notifier.manager::cancelAll) }
            .launchIn(scope)
    }

    override fun postUser(user: User.Complete) {
        if (user.isPropsBlank) {
            onStateChange(ResourceState(error = DONT_EMPTY))
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
        if (id.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseUser.getUser(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUser(id: String, onSucceed: (User.Complete) -> Unit) {
        if (id.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseUser.getUser(id)
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun patchUser(id: String, freshUser: User.Complete) {
        if (id.isBlank() || freshUser.isPropsBlank) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        freshUser.apply { updatedAt = Date() }

        caseUser.patchUser(id, freshUser)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchUser(freshUser: User.Complete, onSucceed: (User.Complete) -> Unit) {
        if (freshUser.id.isBlank() || freshUser.isPropsBlank) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        freshUser.apply { updatedAt = Date() }

        caseUser.patchUser(freshUser.id, freshUser)
            .onEach { onResourceSucceed(it, onSucceed) }.launchIn(viewModelScope)
    }

    override fun deleteUser(id: String) {
        if (id.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseUser.deleteUser(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override val participants = caseUser
        .getUsersParticipation(DEFAULT_BATCH_PARTICIPANT)
        .cachedIn(viewModelScope)

    override fun getUsers(page: Int, size: Int) {
        if (size == 0) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseUser.getUsers(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getMessaging(
        keyName: String, onSucceed: (String) -> Unit
    ) {
        if (keyName.isBlank()) return

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessaging(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit
    ) {
        //if (!messaging.isValid) return

        caseMessaging.addMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun removeMessaging(
        messaging: Messaging.GroupRemover, onSucceed: (String) -> Unit
    ) {
        if (!messaging.isValid) return

        caseMessaging.removeMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun pushMessaging(
        messaging: Messaging.Pusher, onSucceed: (String) -> Unit
    ) {
        if (!messaging.isValid) return

        caseMessaging.pushMessaging(messaging)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    private fun onUserIdChange(fresh: String) {
        prefsFactory.userId = fresh
    }

    private fun onBadgeChange(fresh: Int) {
        prefsFactory.notificationBadge = fresh
    }

    private fun onTokenIdChange(fresh: String) {
        prefsFactory.tokenId = fresh
    }

    fun onUploadProfile() {
        val model: User.Complete? = state.user

        if (formState.profileImage.isEmpty() || model == null) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        model.let { user ->
            val fileId = user.profileUrl.substringAfterLast("/")

            fileCase.uploadFile(formState.profileImage)
                .onEach { res ->
                    onResourceSucceed(res) { file ->
                        val fresh = user.copy(profileUrl = file.url)

                        Log.d("onUploadProfile", fresh.toString())
                        patchUser(id = fresh.id, freshUser = fresh)
                    }
                }
                .flatMapMerge { fileCase.deleteFile(fileId) }
                .launchIn(viewModelScope)
        }
    }

    fun onUpdateUser(fieldKey: String?, fieldValue: String, onSucceed: (User.Complete) -> Unit) {

        if (fieldValue.isBlank() || state.user == null) {
            onStateChange(ResourceState(error = DONT_EMPTY))
        }

        state.user?.let { model ->
            val data = when (fieldKey) {

                IProfileEvent.NAME -> model.copy(
                    fullName = fieldValue
                )

                IProfileEvent.EMAIL -> model.copy(
                    email = fieldValue
                )

                IProfileEvent.PHONE -> model.copy(
                    phone = fieldValue
                )

                IProfileEvent.USERNAME -> model.copy(
                    username = fieldValue
                )

                IProfileEvent.PASSWORD -> model.copy(
                    password = fieldValue
                )

                IProfileEvent.URL_PROFILE -> model.copy(
                    profileUrl = fieldValue
                )

                else -> model
            }

            Log.d("ProfileEditScreen: ", "triggered")
            patchUser(data, onSucceed)
        }
    }
}