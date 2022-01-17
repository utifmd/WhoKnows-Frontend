package com.dudegenuine.whoknows.ui.compose.state

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.dudegenuine.model.User
import java.util.*

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class UserState {

    class FormState: UserState() {
        private val _email = mutableStateOf(TextFieldValue(""))
        val email: TextFieldValue
            get() = _email.value

        private val _password = mutableStateOf(TextFieldValue(""))
        val password: TextFieldValue
            get() = _password.value

        private val _fullName = mutableStateOf(TextFieldValue(""))
        val fullName: TextFieldValue
            get() = _fullName.value

        private val _phone = mutableStateOf(TextFieldValue(""))
        val phone: TextFieldValue
            get() = _phone.value

        private val _rePassword = mutableStateOf(TextFieldValue(""))
        val rePassword: TextFieldValue
            get() = _rePassword.value

        private val _profileImage = mutableStateOf(byteArrayOf())
        val profileImage: ByteArray
            get() = _profileImage.value

        private val _profileUrl = mutableStateOf("")
        val profileUrl: String
            get() = _profileUrl.value

        val isLoginValid: State<Boolean>
            get() = mutableStateOf(
                email.text.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email.text).matches() &&
                password.text.isNotBlank()
            )

        val isRegisValid: State<Boolean>
            get() = mutableStateOf( /*fullName.text.isNotBlank() &&*/ /*phone.text.isNotBlank() &&*/
                email.text.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email.text).matches() &&
                password.text.isNotBlank() &&
                rePassword.text.isNotBlank() &&
                password.text == rePassword.text
            )

        val loginModel: Map<String, String>
            get() = mutableStateMapOf(
                User.EMAIL to email.text,
                User.PASSWORD to password.text
            )

        val regisModel: User get() = mutableStateOf(
            User(
                "USR-${UUID.randomUUID()}",
                fullName.text,
                email.text,
                phone.text,
                email.text.substringBefore("@"),
                password.text,
                profileUrl,
                Date(),
                null
            )
        ).value

        val onUsernameChange: (text: String) -> Unit = {
            _email.value = TextFieldValue(it)
        }

        val onPasswordChange: (text: String) -> Unit = {
            _password.value = TextFieldValue(it)
        }

        /*val onFullNameChange: (text: String) -> Unit = {
            _fullName.value = TextFieldValue(it)
        }*/

        /*val onPhoneChange: (text: String) -> Unit = {
            _phone.value = TextFieldValue(it)
        }*/

        val onRePasswordChange: (text: String) -> Unit = {
            _rePassword.value = TextFieldValue(it)
        }
    }
}
