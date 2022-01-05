package com.dudegenuine.whoknows.ui.presenter

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dudegenuine.model.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class BaseViewModel: ViewModel() {
    protected val _state = mutableStateOf(ResourceState())
    private val TAG: String = javaClass.simpleName

    protected fun<T> resourcing(result: Resource<T>){
        when(result){
            is Resource.Success -> {
                if (result.data is List<*>) {
                    val payload = result.data as List<*>

                    _state.value = ResourceState(
                        users = payload.filterIsInstance<User>(),
                        questions = payload.filterIsInstance<Quiz>()
                    )
                } else _state.value = when(result.data){
                    is User -> ResourceState(user = result.data as User)
                    is Room -> ResourceState(room = result.data as Room)
                    is Quiz -> ResourceState(quiz = result.data as Quiz)
                    is Participant -> ResourceState(participant = result.data as Participant)
                    is Result -> ResourceState(result = result.data as Result)
                    is File -> ResourceState(file = result.data as File)
                    else -> ResourceState()
                }

                Log.d(TAG, "Resource.Success")
            }
            is Resource.Error -> {
                _state.value = ResourceState(
                    error = result.message ?: "An expected error occurred."
                )
                Log.d(TAG, "Resource.ERROR: ${result.message}")
            }
            is Resource.Loading -> {
                _state.value = ResourceState(
                    loading = true
                )
                Log.d(TAG, "Resource.LOADING..")
            }
        }
    }
}