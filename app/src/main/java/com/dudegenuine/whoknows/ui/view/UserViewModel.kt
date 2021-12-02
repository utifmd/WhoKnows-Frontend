package com.dudegenuine.whoknows.ui.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Resource
import com.dudegenuine.usecase.user.GetUser
import com.dudegenuine.whoknows.ui.view.user.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

@HiltViewModel
class UserViewModel( // @Inject
    private val getUserUseCase: GetUser,
    savedStateHandle: SavedStateHandle): ViewModel() {

    private val _state = mutableStateOf(UserState())
    val state: State<UserState> = _state

    init {
        getUser("A00001")
    }

    private fun getUser(userId: String) {
        getUserUseCase(userId).onEach { result ->
            when(result){
                is Resource.Success -> _state.value = UserState(
                    data = result.data )
                is Resource.Error -> _state.value = UserState(
                    error = result.message ?: "An expected error occurred." )
                is Resource.Loading -> _state.value = UserState(
                    loading = true
                )
            }

        }.launchIn(viewModelScope)
    }
}