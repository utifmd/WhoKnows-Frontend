package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralButton
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.misc.FrontLiner
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onRegisterPressed: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val authState = viewModel.authState
    val formState = viewModel.formState

    Column(
        modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        FrontLiner()

        GeneralTextField(
            label = "Enter email or username",
            value = formState.payload.text,
            onValueChange = formState.onUsernameChange,
            leads = Icons.Filled.Email,
            tails = if (formState.payload.text.isNotBlank())
                Icons.Filled.Close else null,
            onTailPressed = { formState.onUsernameChange("") },
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) })
        )

        GeneralTextField(
            label = "Enter password",
            value = formState.password.text,
            asPassword = true,
            leads = Icons.Filled.Password,
            onValueChange = formState.onPasswordChange,
            tails = if (formState.password.text.isNotBlank())
                Icons.Filled.Close else null,
            onTailPressed = { formState.onPasswordChange("") },
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide(); viewModel.signInUser() }))

        if (authState.error.isNotBlank()) {
            ErrorScreen(message = authState.error, isSnack = true)
        }

        Spacer(modifier.height(8.dp))

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