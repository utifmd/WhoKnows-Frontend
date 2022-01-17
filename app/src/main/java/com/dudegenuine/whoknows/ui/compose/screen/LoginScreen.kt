package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun LoginScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()){

    val state = viewModel.state
    val formState = viewModel.formState

    Column(modifier = modifier.padding(16.dp)) {
        if (state.user != null){
            Text(text = state.user.fullName, style = MaterialTheme.typography.h6)
        }
        Spacer(modifier = Modifier.height(8.dp))

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

        if (state.error.isNotBlank()) {
            ErrorScreen(message = state.error, isSnack = true)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            enabled = formState.isLoginValid.value && !state.loading,
            onClick = viewModel::signInUser) {
            Text(text = "Sign In")
        }
    }
}