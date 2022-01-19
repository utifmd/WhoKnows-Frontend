package com.dudegenuine.whoknows.ui.presenter.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
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
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IUserViewModel {
    private val TAG: String = javaClass.simpleName

    private val _uiState = MutableLiveData<UserState>()
    val uiState: LiveData<UserState>
        get() = _uiState

    private val _formState = mutableStateOf(UserState.FormState())
    val formState: UserState.FormState
        get() = _formState.value

    private val _changerState = mutableStateOf(UserState.ChangerState())
    val changerState: UserState.ChangerState
        get() = _changerState.value

    /*init { savedStateHandle.get<String>("field_edit")?.let { fieldId -> formState.onEditFieldIdChange(fieldId) } }*/

    fun onUiStateChange(uiState: UserState){
        if (uiState is UserState.ChangerState) uiState.let {
            _changerState.value = it
        }
        if (uiState is UserState.CurrentState) uiState.let {
            _state.value = state.copy(user = it.freshUser)
        }

        _uiState.value = uiState
    }

    override fun signInUser(onSucceed: (User) -> Unit) {
        val model = formState.loginModel
        if (!formState.isLoginValid.value){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.signInUser(model)
            .onEach { res -> onResourceSucceed(res) { onSucceed(it) }}
            .launchIn(viewModelScope) /*_formState.value = UserState.FormState()*/
    }

    override fun signUpUser(onSucceed: (User) -> Unit) {
        val model = formState.regisModel
        if (!formState.isRegisValid.value){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.postUser(model)
            .onEach { res -> onResourceSucceed(res) { onSucceed(it) }}
            .launchIn(viewModelScope)
    }

    override fun signOutUser(onSucceed: (String) -> Unit) {
        case.signOutUser()
            .onEach { res -> onResourceSucceed(res) { onSucceed(it) }}
            .launchIn(viewModelScope)
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

    override fun patchUser(id: String, current: User) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        case.patchUser(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
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