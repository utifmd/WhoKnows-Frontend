package com.dudegenuine.whoknows.ux.vm.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dudegenuine.model.BuildConfig
import com.dudegenuine.model.Resource
import com.dudegenuine.model.Resource.Companion.NO_RESULT
import com.dudegenuine.model.User
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import com.dudegenuine.repository.contract.dependency.local.IResourceDependency
import com.dudegenuine.repository.contract.dependency.local.IShareLauncher
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.compose.model.Dialog
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.CHECK_CONN
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY_IMG
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.MISS_MATCH
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.USERNAME_EXIST
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import com.dudegenuine.whoknows.ux.compose.state.UserState
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.EMAIL
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.KEY_EDIT_FIELD_TYPE
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.KEY_EDIT_FIELD_VALUE
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.NAME
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.PASSWORD
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.PHONE
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.URL_PROFILE
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.USERNAME
import com.dudegenuine.whoknows.ux.vm.user.contract.IUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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
    private val caseUser: IUserUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    savedStateHandle: SavedStateHandle) : IUserViewModel() {
    @Inject lateinit var notifier: INotifyManager
    @Inject lateinit var share: IShareLauncher
    @Inject lateinit var resource: IResourceDependency
    private val TAG = javaClass.simpleName

    private val prefs get() = caseUser.preferences
    private val _userState = mutableStateOf(UserState())
    val userState get() = _userState.value
    init {
        val navigated = savedStateHandle.get<String>(USER_ID_SAVED_KEY)
        navigated?.let(::getUser) // ?: getUser()
        //if (prefs.userId.isNotBlank()) getUser()
    }
    fun onSharePressed(userId: String) {
        val data = "${BuildConfig.BASE_CLIENT_URL}/who-knows/user/$userId"
        share.launch(data)
    }
    override fun registerUser() {
        if (!userState.isRegisValid.value) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        if (userState.password.text != userState.rePassword.text){
            onStateChange(ResourceState(error = MISS_MATCH))
            return
        }
        if (prefs.tokenId.isBlank()) {
            onStateChange(ResourceState(error = CHECK_CONN))
            return
        }
        caseUser.postUser(userState.regisModel)
            .onEach(::onAuth)
            .launchIn(viewModelScope)
    }
    override fun loginUser() {
        val signer = User.Signer(userState.payload.text, userState.password.text, prefs.tokenId)

        if (!userState.isLoginValid.value) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        if (prefs.tokenId.isBlank()) {
            onStateChange(ResourceState(error = CHECK_CONN))
            return
        }
        caseUser.signInUser(signer) //.onEach{ onAuth(it, onSucceed = ::onSignedIn)}
            .onEach(::onAuth)
            .launchIn(viewModelScope)
    }
    /*private fun onSignedIn(user: User.Complete){
        val option = NavOptions.Builder().setPopUpTo(Screen.Auth.route, true).build()
        onScreenStateChange(ScreenState.Navigate.To(Screen.Home.route, option))
    }*/
    override fun logoutUser() {
        caseUser.signOutUser() //.onEach{ onAuth(it, onSignedOut = ::onSignedOut) }
            .onEach(::onAuth)
            .launchIn(viewModelScope)
    }
    /*private fun onSignedOut(){
        val option = NavOptions.Builder().setPopUpTo(Screen.Home.route, true).build()
        onScreenStateChange(ScreenState.Navigate.To(Screen.Auth.route, option))
    }*/
    fun onAuthDiscoverButtonPressed() = onNavigateTo(Screen.Home.Discover.route)
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
            .onEach(::onResource)
            .launchIn(viewModelScope)
    }
    override fun getUser(id: String) {
        if (id.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        if (id == prefs.userId) {
            caseUser.signInUser(id)
                .onEach(::onAuth)
                .launchIn(viewModelScope)
            return
        }
        Log.d(TAG, "getUser: loose")
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
        freshUser.apply { password = exactPassword }
        caseUser.patchUser(id, freshUser)
            .onEach(::onResource).launchIn(viewModelScope)
    }
    override fun patchUser(freshUser: User.Complete, onSucceed: (User.Complete) -> Unit) {
        if (freshUser.id.isBlank() || freshUser.isPropsBlank) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        freshUser.apply { password = exactPassword }
        Log.d(TAG, "patchUser: freshUser.password = ${freshUser.password}")

        if (freshUser.id == prefs.userId) {
            caseUser.patchUser(freshUser.id, freshUser)
                .onEach{ onResourceSucceed(it, onSucceed) }
                .launchIn(viewModelScope)
            return
        }

        caseUser.patchUser(freshUser.id, freshUser)
            .onEach{ onResourceSucceed(it, onSucceed) }.launchIn(viewModelScope)
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
    private fun onClearAndroidNotifications() {
        notifier.manager.cancelAll()
        /*with(prefs) {
            createMessaging = ""
            addMessaging = false
            removeMessaging = false
        }*/
        //onShowSnackBar("Signed out complete")
    }
    fun onUploadProfile() {
        val user: User.Complete = state.user ?: return
        val fileId = user.profileUrl.substringAfterLast("/")
        if (userState.profileImage.isEmpty()) {
            onStateChange(ResourceState(error = DONT_EMPTY_IMG))
            return
        }
        caseFile.uploadFile(userState.profileImage)
            .flatMapConcat{ onResourceFlow(it) { file ->
                val fresh = user.copy(profileUrl = file.url, createdAt = Date()).apply{
                    password = exactPassword
                }
                flowOf(caseUser.patchUser(user.id, fresh),
                    if(fileId.isNotBlank()) caseFile.deleteFile(fileId) else emptyFlow()).flattenMerge()
                    .map{ Resource.Success(file) }
                }
            }
            .onStart{ _userState.value = UserState() }
            .onCompletion{ if (it != null)
                onToast(it.localizedMessage ?: "error while upload") else
                    getUser(user.id) }
            .launchIn(viewModelScope)
    }
    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: ${auth.user?.email}")
        onScreenStateChange(ScreenState.Navigate.Back())
    }
    override fun onPicturePressed(fileId: String?) {
        if(fileId.isNullOrBlank()) return
        onNavigateTo(Screen.Home.Preview.routeWithArgs(fileId))
    }
    override fun onFullNamePressed(it: String) =
        onNavigateTo(Screen.Home.Setting.ProfileEditor.routeWithArgs(NAME, it))
    override fun onPhonePressed(it: String) =
        onNavigateTo(Screen.Home.Setting.ProfileEditor.routeWithArgs(PHONE, it))
    override fun onEmailPressed(it: String) =
        onNavigateTo(Screen.Home.Setting.ProfileEditor.routeWithArgs(EMAIL, it))
    override fun onUsernamePressed(it: String) =
        onNavigateTo(Screen.Home.Setting.ProfileEditor.routeWithArgs(USERNAME, it))
    override fun onPasswordPressed(it: String) =
        onNavigateTo(Screen.Home.Setting.ProfileEditor.routeWithArgs(PASSWORD, it))
    override fun onSignOutPressed() {
        val dialog = ScreenState.AlertDialog(
            Dialog(resource.string(R.string.logout_account),
            onSubmitted = ::logoutUser)
        )
        onScreenStateChange(dialog)
    }
    private val fieldValue = savedStateHandle.get<String>(KEY_EDIT_FIELD_VALUE) ?: EMPTY_STRING
    val fieldKey = savedStateHandle.get<String>(KEY_EDIT_FIELD_TYPE) ?: NO_RESULT

    fun onUpdateUser(
        currentUser: User.Complete) {
        if (fieldKey == NO_RESULT && fieldValue.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        val data = when (fieldKey) {
            NAME -> currentUser.copy(fullName = fieldValue)
            EMAIL -> currentUser.copy(email = fieldValue)
            PHONE -> currentUser.copy(phone = fieldValue)
            USERNAME -> currentUser.copy(username = fieldValue)
            PASSWORD -> currentUser.copy(password = fieldValue)
            URL_PROFILE -> currentUser.copy(profileUrl = fieldValue)
            else -> currentUser
        }
        if (fieldKey == USERNAME && fieldValue != currentUser.username){
            Log.d(TAG, "onUpdateUser: isUsernameUsed trigger")
            caseUser.isUsernameUsed(fieldValue)
                .onEach{ onResourceSucceed(it){ isUsed ->
                    if (isUsed) onStateChange(ResourceState(error = USERNAME_EXIST))
                    else patchUser(data, ::onUpdatedUser) }}
                .launchIn(viewModelScope)
            return
        }
        patchUser(data, ::onUpdatedUser)
    }
    private fun onUpdatedUser(user: User.Complete){
        Log.d(TAG, "onUpdatedUser: trigger")
        onAuthChange(ResourceState.Auth(user = user))
        onNavigateBack()
    }
}