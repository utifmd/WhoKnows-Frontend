package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()){

    val state = viewModel.state
    val formState = viewModel.formState

    Column(
        modifier = modifier.padding(16.dp)) {
        GeneralTextField(
            label = "Enter email",
            value = formState.email.text,
            onValueChange = formState.onUsernameChange,
            leads = Icons.Default.Email,
            tails = if (formState.email.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onUsernameChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Enter password",
            value = formState.password.text,
            asPassword = true,
            leads = Icons.Default.Password,
            onValueChange = formState.onPasswordChange,
            tails = if (formState.password.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onPasswordChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Confirm password",
            value = formState.rePassword.text,
            asPassword = true,
            leads = Icons.Default.Password,
            onValueChange = formState.onRePasswordChange,
            tails = if (formState.rePassword.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onRePasswordChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (state.error.isNotBlank()) {
            ErrorScreen(message = state.error, isSnack = true)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            enabled = formState.isRegisValid.value && !state.loading,
            onClick = viewModel::signUpUser) {
            Text(text = stringResource(R.string.sign_up))
        }

        /*GeneralTextField(
            label = "Enter full name",
            value = formState.fullName.text,
            onValueChange = formState.onFullNameChange,
            leads = Icons.Default.Person,
            tails = if (formState.fullName.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onFullNameChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        GeneralTextField(
            label = "Enter phone number",
            value = formState.phone.text,
            onValueChange = formState.onPhoneChange,
            leads = Icons.Default.Phone,
            tails = if (formState.phone.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onPhoneChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))*/
    }
}