package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Clear
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
import com.dudegenuine.whoknows.ux.compose.component.GeneralButton
import com.dudegenuine.whoknows.ux.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ux.compose.component.misc.FrontLiner
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    auth: ResourceState.Auth,
    viewModel: UserViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState()){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val formState = viewModel.userState

    Box(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)){
        Column(modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            FrontLiner()

            GeneralTextField(
                label = "Enter email",
                value = formState.payload.text,
                onValueChange = formState.onUsernameChange,
                isError = auth.error.isNotBlank(),
                leads = Icons.Filled.Email,
                trail = if (formState.payload.text.isNotBlank())
                    Icons.Filled.Clear else null,
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
                isError = auth.error.isNotBlank(),
                trail = if (formState.password.text.isNotBlank())
                    Icons.Filled.Clear else null,
                onTailPressed = { formState.onPasswordChange("") },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.moveFocus(FocusDirection.Down) })
            )
            GeneralTextField(
                label = "Confirm password",
                value = formState.rePassword.text,
                asPassword = true,
                leads = Icons.Filled.Password,
                onValueChange = formState.onRePasswordChange,
                isError = auth.error.isNotBlank(),
                trail = if (formState.rePassword.text.isNotBlank())
                    Icons.Filled.Clear else null,
                onTailPressed = { formState.onRePasswordChange("") },
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); viewModel.registerUser() })
            )

            if (auth.error.isNotBlank())
                ErrorScreen(message = auth.error, isSnack = true)

            GeneralButton(
                label = stringResource(R.string.sign_up),
                enabled = formState.isRegisValid.value && !auth.loading,
                leadingIcon = Icons.Default.AppRegistration,
                isLoading = auth.loading,
                onClick = viewModel::registerUser
            )

            /*GeneralTextField(
                label = "Enter full name",
                value = formState.fullName.text,
                onValueChange = formState.onFullNameChange,
                leads = Icons.Filled.Person,
                tails = if (formState.fullName.text.isNotBlank())
                    Icons.Filled.Clear else null,
                onTailPressed = { formState.onFullNameChange("") }
            )
            GeneralTextField(
                label = "Enter phone number",
                value = formState.phone.text,
                onValueChange = formState.onPhoneChange,
                leads = Icons.Filled.Phone,
                tails = if (formState.phone.text.isNotBlank())
                    Icons.Filled.Clear else null,
                onTailPressed = { formState.onPhoneChange("") }
            )*/
        }
    }
}