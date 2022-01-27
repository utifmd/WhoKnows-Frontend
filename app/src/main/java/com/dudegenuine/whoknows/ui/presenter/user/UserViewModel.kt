package com.dudegenuine.whoknows.ui.presenter.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.compose.state.UserState
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.presenter.user.contract.IUserViewModel
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
        private val case: IUserUseCaseModule,
        private val fileCase: IFileUseCaseModule,
        private val savedStateHandle: SavedStateHandle): BaseViewModel(), IUserViewModel {

    private val _formState = mutableStateOf(UserState.FormState())
    val formState: UserState.FormState
        get() = _formState.value

    init { getUser() }

    override fun signInUser() {

        val model = formState.loginModel
        if (!formState.isLoginValid.value){
            _authState.value = ResourceState.Auth(error = DONT_EMPTY)
            return
        }

        case.signInUser(model)
            .onEach(this::onAuth).launchIn(viewModelScope)
    }

    override fun signUpUser() {
        val model = formState.regisModel
        if (!formState.isRegisValid.value){
            _authState.value = ResourceState.Auth(error = DONT_EMPTY)
            return
        }

        case.postUser(model)
            .onEach(this::onAuth).launchIn(viewModelScope)
    }

    override fun signOutUser() {
        case.signOutUser() /*.onEach { res -> onResourceSucceed(res) { onSucceed(it) }} .launchIn(viewModelScope)*/
            .onEach(this::onAuth).launchIn(viewModelScope)
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

    fun onUploadProfile(){
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

    override fun postUser(user: User) {
        if (user.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.postUser(user)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUser() {
        case.getUser()
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUser(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getUser(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchUser(id: String, freshUser: User) {
        if (id.isBlank() || freshUser.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        freshUser.apply { updatedAt = Date() }

        case.patchUser(id, freshUser)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchUser(freshUser: User, onSucceed: (User) -> Unit) {
        if (freshUser.id.isBlank() || freshUser.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        freshUser.apply { updatedAt = Date() }

        case.patchUser(freshUser.id, freshUser)
            .onEach { onResourceSucceed(it, onSucceed) }.launchIn(viewModelScope)
    }

    override fun deleteUser(id: String) {
        if (id.isBlank()) {
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.deleteUser(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getUsers(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getUsers(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}


/*private val TAG: String = javaClass.simpleName*/
/*private val _uiState = MutableLiveData<UserState>()
val uiState: LiveData<UserState>
    get() = _uiState*/
/*private lateinit var initUiState: UserState*/
/*{
    savedStateHandle.get<String>("user")?.let {
        val user = mapper.asUser(it)

        Log.d(TAG, user.email)
        onUiStateChange(initUiState)

        _state.value = state.copy()
    }
}*/

/*fun onUiStateChange(uiState: UserState){
    if (uiState is UserState.ChangerState) uiState.let {
        _changerState.value = it
    }

    if (uiState is UserState.CurrentState) uiState.let {
        _state.value = state.copy(user = it.freshUser)
    }

    _uiState.value = uiState
}*/
