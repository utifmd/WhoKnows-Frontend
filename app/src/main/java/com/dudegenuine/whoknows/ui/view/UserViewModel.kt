package com.dudegenuine.whoknows.ui.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Resource
import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.ui.view.user.UserState
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
        private val getUsersUseCase: GetUsers,
        private val signInUsersUseCase: SignInUser //, savedStateHandle: SavedStateHandle
    ): ViewModel(), IUserViewModel {

    private val _state = mutableStateOf(UserState())
    val state: State<UserState> = _state

    init {
        getUsers(0, 10)
        // getUser("USR00001")
    }


    private fun<T> resourcing(result: Resource<T>){
        when(result){
            is Resource.Success -> {
                if (result.data is List<*>) _state.value = UserState(
                    users = result.data as List<User>
                ) else _state.value = UserState(
                    user = result.data as User
                )
            }
            is Resource.Error -> _state.value = UserState(
                error = result.message ?: "An expected error occurred." )
            is Resource.Loading -> _state.value = UserState(
                loading = true
            )
        }
    }

    override fun signInUser(loginRequest: LoginRequest) {
        signInUsersUseCase(loginRequest).onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun postUser(user: User) {
        postUserUseCase(user).onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getUser(id: String) {
        getUserUseCase(id).onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchUser(id: String, current: User) {
        patchUserUseCase(id, current).onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteUser(id: String) {
        deleteUserUseCase(id).onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getUsers(page: Int, size: Int) {
        getUsersUseCase(page, size).onEach(this::resourcing).launchIn(viewModelScope)
    }
}