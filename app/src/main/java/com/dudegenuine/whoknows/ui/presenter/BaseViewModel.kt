package com.dudegenuine.whoknows.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dudegenuine.model.Resource
import com.dudegenuine.model.User

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class BaseViewModel: ViewModel() {
    protected val _state = mutableStateOf(ViewState())

    protected fun<T> resourcing(result: Resource<T>){
        when(result){
            is Resource.Success -> {
                if (result.data is List<*>) {
                    val payload = result.data as List<*>

                    _state.value = ViewState(
                        users = payload.filterIsInstance<User>()
                    )
                } else _state.value = ViewState(
                    user = result.data as User
                )
            }
            is Resource.Error -> _state.value = ViewState(
                error = result.message ?: "An expected error occurred." )
            is Resource.Loading -> _state.value = ViewState(
                loading = true
            )
        }
    }
}