package com.dudegenuine.whoknows.ui.presenter

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Resource
import com.dudegenuine.model.User

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
                } else _state.value = ViewState(
                    user = result.data as User,
                    quiz = result.data as Quiz
                )
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