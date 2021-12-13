package com.dudegenuine.whoknows.ui.view.user

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.ViewState
import com.dudegenuine.whoknows.ui.view.ViewState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.view.user.contract.IUserViewModel
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
    private val postUserUseCase: PostUser,
    private val getUserUseCase: GetUser,
    private val patchUserUseCase: PatchUser,
    private val deleteUserUseCase: DeleteUser,
    private val getUsersUseCase: GetUsers, //, savedStateHandle: SavedStateHandle
    private val signInUsersUseCase: SignInUser): BaseViewModel(), IUserViewModel {

    val state: State<ViewState> = _state // init { getUsers(0, 10); getUser("USR00002") }

    override fun signInUser(loginRequest: LoginRequest) {
        if (loginRequest.email.isEmpty() ||
            loginRequest.password.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        signInUsersUseCase(loginRequest)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun postUser(user: User) {
        if (user.fullName.isEmpty() ||
            user.email.isEmpty() ||
            user.phone.isEmpty() ||
            user.username.isEmpty() ||
            user.password.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        user.apply { createdAt = Date() }

        postUserUseCase(user)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getUser(id: String) {
        if (id.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getUserUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchUser(id: String, current: User) {
        if (id.isEmpty() ||
            current.fullName.isEmpty() ||
            current.email.isEmpty() ||
            current.phone.isEmpty() ||
            current.username.isEmpty() ||
            current.password.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchUserUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteUser(id: String) {
        if (id.isEmpty()) {
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        deleteUserUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getUsers(page: Int, size: Int) {
        if (size != 0){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getUsersUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}