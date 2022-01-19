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
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralButton
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.route.Screen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    router: NavHostController){
    val TAG = "LoginScreen"

    val state = viewModel.state
    val formState = viewModel.formState

    val onRegisterPressed: () -> Unit = {
        router.navigate(Screen.AuthScreen.RegisScreen.route)
    }

    val onSignInPressed: () -> Unit = {
        viewModel.signInUser {
            router.navigate(Screen.MainScreen.SummaryScreen.route)
        }
    }

    Column(
        modifier = modifier.padding(16.dp)) {

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

        if (state.error.isNotBlank()) {
            ErrorScreen(message = state.error, isSnack = true)
        }

        Spacer(modifier = Modifier.height(8.dp))
        GeneralButton(
            label = stringResource(R.string.sign_in),
            enabled = formState.isLoginValid.value && !state.loading,
            isLoading = state.loading,
            onClick = onSignInPressed
        )

        TextButton(onClick = onRegisterPressed) {
            Text(text = stringResource(R.string.have_no_account_yet), color = MaterialTheme.colors.primary)
        }
    }
}