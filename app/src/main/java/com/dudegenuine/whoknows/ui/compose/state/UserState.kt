package com.dudegenuine.whoknows.ui.compose.state

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.dudegenuine.model.User
import com.dudegenuine.model.common.ImageUtil
import java.util.*

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class UserState {
    class FormState: UserState() {
        private val _payload = mutableStateOf(TextFieldValue(""))
        val payload: TextFieldValue
            get() = _payload.value

        private val _password = mutableStateOf(TextFieldValue(""))
        val password: TextFieldValue
            get() = _password.value

        private val _rePassword = mutableStateOf(TextFieldValue(""))
        val rePassword: TextFieldValue
            get() = _rePassword.value

        private val _profileImage = mutableStateOf(byteArrayOf())
        val profileImage: ByteArray
            get() = _profileImage.value

        val isLoginValid: State<Boolean>
            get() = mutableStateOf(
                payload.text.isNotBlank() &&
                        password.text.isNotBlank()
            )

        val isRegisValid: State<Boolean>
            get() = mutableStateOf( /*fullName.text.isNotBlank() &&*/ /*phone.text.isNotBlank() &&*/
                payload.text.isNotBlank() &&
                        Patterns.EMAIL_ADDRESS.matcher(payload.text).matches() &&
                        password.text.isNotBlank() &&
                        rePassword.text.isNotBlank() &&
                        password.text == rePassword.text
            )

        val loginModel: Map<String, String>
            get() = mutableStateMapOf(
                User.PAYLOAD to payload.text,
                User.PASSWORD to password.text
            )

        val regisModel: User
            get() = mutableStateOf(
            User(
                "USR-${UUID.randomUUID()}",
                "",
                payload.text,
                "",
                payload.text.substringBefore("@"),
                password.text,
                "",
                Date(),
                null,
                emptyList(),
                emptyList(),
                emptyList(),
            )
        ).value

        val onUsernameChange: (text: String) -> Unit = {
            _payload.value = TextFieldValue(it)
        }

        val onPasswordChange: (text: String) -> Unit = {
            _password.value = TextFieldValue(it)
        }

        val onRePasswordChange: (text: String) -> Unit = {
            _rePassword.value = TextFieldValue(it)
        }

        fun onImageValueChange(uri: Uri?, context: Context) {
            uri?.let {
                val scaledImage = ImageUtil.adjustImage(context, uri)

                _profileImage.value = scaledImage
            }
        }

        /*class Navigator {
            private val _sharedFlow =
              MutableSharedFlow<NavTarget>(extraBufferCapacity = 1)
            val sharedFlow = _sharedFlow.asSharedFlow()

            fun navigateTo(navTarget: NavTarget) {
                _sharedFlow.tryEmit(navTarget)
            }

            enum class NavTarget(val label: String) {

                Home("home"),
                Detail("detail")
            }
        }*/

        /*private val _fullName = mutableStateOf(TextFieldValue(""))
        val fullName: TextFieldValue
            get() = _fullName.value

        private val _phone = mutableStateOf(TextFieldValue(""))
        val phone: TextFieldValue
            get() = _phone.value

        private val _email = mutableStateOf(TextFieldValue(""))
        val email: TextFieldValue
            get() = _email.value

        val onFullNameChange: (text: String) -> Unit = {
            _fullName.value = TextFieldValue(it)
        }

        val onPhoneChange: (text: String) -> Unit = {
            _phone.value = TextFieldValue(it)
        }

        val onProfileUrlChange: (text: String) -> Unit = {
            _profileUrl.value = it
        }

        private val _profileImage = mutableStateOf(byteArrayOf())
        val profileImage: ByteArray
            get() = _profileImage.value

        val onEmailChange: (text: String) -> Unit = {
            _email.value = TextFieldValue(it)
        }*/
    }

    /*data class CurrentState(val freshUser: User): UserState()
    data class ChangerState(val currentUser: User? = null, val fieldKey: String? = null): UserState(){
        val selectedFieldValue = when(fieldKey) {
            NAME -> currentUser?.fullName
            PHONE -> currentUser?.phone
            EMAIL -> currentUser?.email
            else -> "No value"
        }

        val model: User get() = mutableStateOf(
            currentUser?.copy() ?: User(
                "USR-${UUID.randomUUID()}","", "", "", "", "", "", Date(), Date()
            )
        ).value
    }
    data class Auth(
        val loading: Boolean = false,
        val error: String = ""): UserState()*/
}
