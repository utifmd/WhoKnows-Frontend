package com.dudegenuine.whoknows.ui.view.user

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.ViewState
import com.dudegenuine.whoknows.ui.view.user.contract.IUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    val state: State<ViewState> = _state

    // init { getUsers(0, 10); getUser("USR00002") }

    override fun signInUser(loginRequest: LoginRequest) {
        signInUsersUseCase(loginRequest)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun postUser(user: User) {
        postUserUseCase(user)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getUser(id: String) {
        getUserUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchUser(id: String, current: User) {
        patchUserUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteUser(id: String) {
        deleteUserUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getUsers(page: Int, size: Int) {
        getUsersUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}