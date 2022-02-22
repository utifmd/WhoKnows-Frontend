package com.dudegenuine.whoknows.ui.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dudegenuine.model.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class BaseViewModel: ViewModel() {
    private val TAG: String = javaClass.simpleName

    protected val _state = mutableStateOf(ResourceState())
    val state: ResourceState
        get() = _state.value

    protected val _authState = mutableStateOf(ResourceState.Auth())
    val authState: ResourceState.Auth
        get() = _authState.value

    protected fun<T> onResource(resource: Resource<T>){
        onResourceSucceed(resource){ data ->
            if (data is List<*>) {
                val payload = data as List<*>

                val initialState = ResourceState(
                    users = payload.filterIsInstance<User>(),
                    rooms = payload.filterIsInstance<Room>(),
                    questions = payload.filterIsInstance<Quiz>(),
                    results = payload.filterIsInstance<Result>(),
                    participants = payload.filterIsInstance<Participant>(),
                    notifications = payload.filterIsInstance<Notification>(),
                    files = payload.filterIsInstance<File>(),
                )

                _state.value = if(payload.isEmpty())
                    initialState.copy(error = "No result.")
                else initialState

            } else _state.value = when(data) {
                is User -> ResourceState(user = data as User)
                is Room -> ResourceState(room = data as Room)
                is Quiz -> ResourceState(quiz = data as Quiz)
                is Participant -> ResourceState(participant = data as Participant)
                is Notification -> ResourceState(notification = data as Notification)
                is Result -> ResourceState(result = data as Result)
                is File -> ResourceState(file = data as File)
                is String -> ResourceState(message = data)
                else -> ResourceState()
            }

            Log.d(TAG, "Resource.Success")
        }
    }

    protected fun<T> onResourceSucceed(resources: Resource<T>, onSuccess: (T) -> Unit){
        when(resources){
            is Resource.Success -> resources.data?.let { onSuccess(it) } ?: also {
                Log.d(TAG, "Resource.Error: ${resources.message}")
                _state.value = ResourceState(
                    error = resources.message ?: "Resource data is null."
                )
            }

            is Resource.Error -> {
                Log.d(TAG, "Resource.Error: ${resources.message}")
                _state.value = ResourceState(
                    error = resources.message ?: "An unexpected error occurred."
                )
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.Loading..")
                _state.value = ResourceState(
                    loading = true
                )
            }
        }
    }

    protected fun<T> onResourceSuccess(resources: Resource<T>, onSucceed: (T) -> Unit){
        if(resources is Resource.Success){
            resources.data?.let { onSucceed(it) }
        }
    }

    protected fun<T> onAuth(resources: Resource<T>){
        when(resources){
            is Resource.Success -> { /*_authState.value = when(resources.data) { is User -> _root_ide_package_.com.dudegenuine.whoknows.ui.presenter.ResourceState.Auth( currentUser = resources.data as User ) else -> _root_ide_package_.com.dudegenuine.whoknows.ui.presenter.ResourceState.Auth() }*/

                _state.value = when(resources.data) {
                    is User -> ResourceState(
                        user = resources.data as User)

                    is String -> ResourceState( // user signed out
                        user = null,
                        error = resources.message ?: "An unexpected error occurred.").also {

                        _authState.value = ResourceState.Auth() }
                    else -> ResourceState()
                }
            }

            is Resource.Error -> {
                Log.d(TAG, "Auth.Error: ${resources.message}")
                _authState.value = ResourceState.Auth(
                    error = resources.message ?: "An unexpected error occurred."
                )
            }
            is Resource.Loading -> {
                Log.d(TAG, "Auth.Loading..")
                _authState.value = ResourceState.Auth(
                    loading = true
                )
            }
        }
    }
}