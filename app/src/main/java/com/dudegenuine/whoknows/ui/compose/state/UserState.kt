package com.dudegenuine.whoknows.ui.compose.state

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
data class UserState(
    val username: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf("")){
    private val TAG: String = javaClass.simpleName

    val isValid: MutableState<Boolean>
        get() = mutableStateOf(
            username.value.isNotBlank() &&
                    password.value.isNotBlank())

    val onUsernameChange: (text: String) -> Unit = {
        username.value = it
    }

    val onPasswordChange: (text: String) -> Unit = {
        password.value = it
    }
}
