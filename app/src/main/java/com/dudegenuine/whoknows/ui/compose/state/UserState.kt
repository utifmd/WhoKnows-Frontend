package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.dudegenuine.model.User

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class UserState {

    class CreateState {
        private val _email = mutableStateOf(TextFieldValue(""))
        val email: TextFieldValue
            get() = _email.value

        private val _password = mutableStateOf(TextFieldValue(""))
        val password: TextFieldValue
            get() = _password.value

        val isValid: State<Boolean>
            get() = mutableStateOf(
                email.text.isNotBlank() &&
                        password.text.isNotBlank())

        val model: Map<String, String>
            get() = mutableStateMapOf(
                User.EMAIL to email.text,
                User.PASSWORD to password.text
            )

        val onUsernameChange: (text: String) -> Unit = {
            _email.value = TextFieldValue(it)
        }

        val onPasswordChange: (text: String) -> Unit = {
            _password.value = TextFieldValue(it)
        }
    }
}
