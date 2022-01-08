package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
data class UserState(
    val email: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf("")){
    private val TAG: String = javaClass.simpleName

    val isValid: MutableState<Boolean>
        get() = mutableStateOf(
            email.value.isNotBlank() &&
                    password.value.isNotBlank())

    val onUsernameChange: (text: String) -> Unit = {
        email.value = it
    }

    val onPasswordChange: (text: String) -> Unit = {
        password.value = it
    }
}
