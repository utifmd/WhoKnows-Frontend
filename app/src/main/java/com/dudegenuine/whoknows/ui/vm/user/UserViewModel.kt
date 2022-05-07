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
import com.dudegenuine.model.Resource
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
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY_IMG
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.MISS_MATCH
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class UserViewModel
    @Inject constructor(
    private val prefsFactory: IPrefsFactory,
    private val caseMessaging: IMessageUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    private val savedStateHandle: SavedStateHandle) : IUserViewModel() {
    private val TAG = javaClass.simpleName
    @Inject
    lateinit var notifier: INotifyManager
    @Inject
    lateinit var share: IShareLauncher

    private val _formState = mutableStateOf(UserState.FormState())
    val formState: UserState.FormState
        get() = _formState.value

    init {
        val navigated = savedStateHandle.get<String>(USER_ID_SAVED_KEY)

        navigated?.let(this::getUser) ?: getUser()
        if (prefsFactory.userId.isNotBlank()) getUser()
    }

    fun onSharePressed(userId: String) {
        val data = "${BuildConfig.BASE_CLIENT_URL}/who-knows/user/$userId"
        share.launch(data)
    }

    private fun onRegisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                caseMessaging.addMessaging(
                    Messaging.GroupAdder(roomId, listOf(prefsFactory.tokenId), key)) }}
            .onEach(::onResourceStateless)

    private fun onUnregisterMessaging(roomId: String) =
        caseMessaging.getMessaging(roomId)
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .flatMapConcat { res -> onResourceFlow(res) { key ->
                caseMessaging.removeMessaging(
                    Messaging.GroupRemover(roomId, listOf(prefsFactory.tokenId), key)) }}
            .onEach(::onResourceStateless)

    override fun registerUser() {
        val model = formState.regisModel
        if (!formState.isRegisValid.value) {
            onAuthStateChange(ResourceState.Auth(error = DONT_EMPTY))
            return
        }

        if (formState.password.text != formState.rePassword.text){
            onAuthStateChange(ResourceState.Auth(error = MISS_MATCH))
            return
        }

        if (prefsFactory.tokenId.isBlank()) {
            onAuthStateChange(ResourceState.Auth(error = CHECK_CONN))
            return
        }

        caseUser.postUser(model)
            .flatMapLatest { res -> onResourceAuthFlow(res) { currentUser ->
                val joined = currentUser.participants.map { it.roomId }
                val owned = currentUser.rooms.map { it.roomId }

                concatenate(joined, owned).asFlow()
                    .flatMapConcat(::onRegisterMessaging)
                    .mapLatest { Resource.Success(currentUser) }}}

            .onEach { res -> onResourceStateless(res, ::signInUser) }
            .onEmpty { state.user?.let(::signInUser) }
            .onCompletion { it?.let(::onResolveAddMessaging) }
            .catch { it.let(::onResolveAddMessaging) }
            .retryWhen { cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(viewModelScope)
    }

    override fun loginUser() {
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
            .flatMapConcat{ res -> onResourceAuthFlow(res) { currentUser ->
                val joined = currentUser.participants.map { it.roomId }
                val owned = currentUser.rooms.map { it.roomId }

                concatenate(joined, owned).asFlow()
                    .flatMapConcat(::onRegisterMessaging)
                    .mapLatest{ Resource.Success(currentUser) }}}

            .onEach{ res -> onResourceStateless(res, ::signInUser) }
            .onEmpty{ state.user?.let(::signInUser) }
            .onCompletion{ it?.let(::onResolveAddMessaging) }
            .catch{ it.let(::onResolveAddMessaging) }
            .retryWhen{ cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(viewModelScope)
    }

    private fun signInUser(model: User.Complete) {
        caseUser.signInUser(model)
            .onEach(::onAuth)
            .onStart{ model.notifications
                .count{ !it.seen }
                .let(::onBadgeChange) }
            .launchIn(viewModelScope)
    }

    override fun logoutUser() {
        val userId = prefsFactory.userId
        if (prefsFactory.tokenId.isBlank() or userId.isBlank()) {
            onAuthStateChange(ResourceState.Auth(error = CHECK_CONN))
            return
        }

        caseUser.getUser(userId)
            .flatMapConcat{ res -> onResourceAuthFlow(res){ currentUser ->
                val joined = currentUser.participants.map{ it.roomId }
                val owned = currentUser.rooms.map{ it.roomId }

                concatenate(joined, owned).asFlow()
                    .flatMapConcat(::onUnregisterMessaging)
                    .mapLatest{ Resource.Success(currentUser) }}}

            .onEach{ res -> onResourceStateless(res, ::signOutUser) }
            .onEmpty{ state.user?.let(::signOutUser) }
            .onCompletion{ it?.let(::onResolveRemoveMessaging) }
            .catch{ it.let(::onResolveRemoveMessaging) }
            .retryWhen{ cause, attempt -> cause is IOException || attempt < 3 }
            .launchIn(viewModelScope)
    }

    private fun signOutUser(user: User.Complete) {
        caseUser.signOutUser(user)
            .onStart { onRejectAllMessaging() }
            .launchIn(viewModelScope)
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
            .onEach(::onResource).launchIn(viewModelScope)
    }

    override fun getUser(id: String) {
        if (id.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseUser.getUser(id)
            .onEach(::onResource).launchIn(viewModelScope)
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
            .onEach(::onResource).launchIn(viewModelScope)
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
            .onEach(::onResource).launchIn(viewModelScope)
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
            .onEach(::onResource).launchIn(viewModelScope)
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
        //if (!messaging.isValid) return

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

    /*private fun onUserIdChange(fresh: String) {
        prefsFactory.userId = fresh
    }*/

    private fun onBadgeChange(fresh: Int) {
        prefsFactory.notificationBadge = fresh
    }

    private fun onRejectAllMessaging() {
        Log.d(TAG, "onRejectAllMessaging: triggered")
        notifier.manager.cancelAll()

        with(prefsFactory) {
            createMessaging = ""
            addMessaging = false
            removeMessaging = false
        }
    }

    /*private fun onTokenIdChange(fresh: String) {
        prefsFactory.tokenId = fresh
    }*/

    private fun onResolveAddMessaging(t: Throwable) {
        Log.d(TAG, "onResolveAddMessaging: ${t.message}")
        prefsFactory.addMessaging = true
    }

    private fun onResolveRemoveMessaging(t: Throwable) {
        Log.d(TAG, "onResolveRemoveMessaging: ${t.message}")
        prefsFactory.removeMessaging = true
    }

    /*private fun onReportUnregisterMessaging(t: Throwable) {
        Log.d(TAG, "onReportUnregisterMessaging: ${t.message}")
        prefsFactory.unregisterMessaging = true
    }*/

    fun onUploadProfile() {
        val user: User.Complete = state.user ?: return
        val fileId = user.profileUrl.substringAfterLast("/")

        if (formState.profileImage.isEmpty()) {
            onStateChange(ResourceState(error = DONT_EMPTY_IMG))
            return
        }
        caseFile.uploadFile(formState.profileImage)
            .flatMapConcat { onResourceFlow(it) { file ->
                val fresh = user.copy(profileUrl = file.url, createdAt = Date())
                flowOf(caseUser.patchUser(user.id, fresh),
                    if(fileId.isNotBlank()) caseFile.deleteFile(fileId) else emptyFlow()).flattenMerge()
                    .map { Resource.Success(file) }
                }
            }
            .onStart { _formState.value = UserState.FormState() }
            .onCompletion { it?.let(::onResolveAddMessaging) } // onStateChange(ResourceState(message = it.localizedMessage ?: Resource.NO_RESULT))
            .catch { it.let(::onResolveAddMessaging) }
            .launchIn(viewModelScope)
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