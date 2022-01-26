package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.dudegenuine.whoknows.ui.compose.component.GeneralButton
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    onRegisterPressed: () -> Unit
) {
    val authState = viewModel.authState
    val formState = viewModel.formState

    Column(
        modifier = modifier.padding(16.dp)) {

        Spacer(
            modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Enter email",
            value = formState.email.text,
            onValueChange = formState.onUsernameChange,
            leads = Icons.Default.Email,
            tails = if (formState.email.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onUsernameChange("") }
        )

        Spacer(
            modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Enter password",
            value = formState.password.text,
            asPassword = true,
            leads = Icons.Default.Password,
            onValueChange = formState.onPasswordChange,
            tails = if (formState.password.text.isNotBlank())
                Icons.Default.Close else null,
            onTailPressed = { formState.onPasswordChange("") })

        if (authState.error.isNotBlank()) {
            ErrorScreen(message = authState.error, isSnack = true)
        }

        Spacer(
            modifier = Modifier.height(8.dp))

        GeneralButton(
            label = stringResource(R.string.sign_in),
            enabled = formState.isLoginValid.value && !authState.loading,
            isLoading = authState.loading,
            onClick = viewModel::signInUser
        )

        TextButton(
            onClick = onRegisterPressed) {
            Text(
                text = stringResource(R.string.have_no_account_yet),
                color = MaterialTheme.colors.primary
            )
        }
    }
}