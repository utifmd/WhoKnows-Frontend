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
    protected val _state = mutableStateOf(ViewState())
    private val TAG: String = javaClass.simpleName

    protected fun<T> resourcing(result: Resource<T>){
        when(result){
            is Resource.Success -> {
                Log.d(TAG, "Resource.Success")
                if (result.data is List<*>) {
                    val payload = result.data as List<*>

                    _state.value = ViewState(
                        users = payload.filterIsInstance<User>(),
                        questions = payload.filterIsInstance<Quiz>()
                    )
                } else _state.value = when(result.data){
                    is User -> ViewState(user = result.data as User)
                    is Room ->ViewState(room = result.data as Room)
                    is Quiz -> ViewState(quiz = result.data as Quiz)
                    is Participant -> ViewState(participant = result.data as Participant)
                    is Result -> ViewState(result = result.data as Result)
                    else -> ViewState()
                }
            }
            is Resource.Error -> {
                Log.d(TAG, "Resource.ERROR: ${result.message}")

                _state.value = ViewState(
                    error = result.message ?: "An expected error occurred."
                )
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.LOADING..")

                _state.value = ViewState(
                    loading = true
                )
            }
        }
    }
}