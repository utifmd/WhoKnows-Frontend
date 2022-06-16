package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.Explore
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
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun LoginScreen(
    modifier: Modifier = Modifier,
    auth: ResourceState.Auth,
    viewModel: UserViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onRegisterPressed: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val formState = viewModel.formState

    Box(modifier.verticalScroll(scrollState)){
        Column(
            modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            FrontLiner()
            OutlinedButton(
                onClick = viewModel::onLoginDiscoverPressed, modifier = Modifier.fillMaxSize()){
                Icon(imageVector = Icons.Outlined.Explore, contentDescription = null)
                Spacer(modifier.size(ButtonDefaults.IconSize))
                Text(text = stringResource(R.string.discover))
            }
            Spacer(modifier.size(24.dp))
            GeneralTextField(
                label = "Enter email or username",
                value = formState.payload.text,
                isError = auth.error.isNotBlank(),
                onValueChange = formState.onUsernameChange,
                leads = Icons.Filled.Email,
                tails = if (formState.payload.text.isNotBlank())
                    Icons.Filled.Clear else null,
                onTailPressed = {
                    formState.onUsernameChange("")
                    viewModel.onAuthChange(ResourceState.Auth()) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.moveFocus(FocusDirection.Down) })
            )

            GeneralTextField(
                label = "Enter password",
                value = formState.password.text,
                asPassword = true,
                isError = auth.error.isNotBlank(),
                leads = Icons.Filled.Password,
                onValueChange = formState.onPasswordChange,
                tails = if (formState.password.text.isNotBlank())
                    Icons.Filled.Clear else null,
                onTailPressed = {
                    formState.onPasswordChange("")
                    viewModel.onAuthChange(ResourceState.Auth()) },
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); viewModel.loginUser() })
            )
            GeneralButton(
                label = stringResource(R.string.sign_in),
                enabled = formState.isLoginValid.value && !auth.loading,
                isLoading = auth.loading,
                onClick = viewModel::loginUser,
                modifier = Modifier.fillMaxWidth()
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
}