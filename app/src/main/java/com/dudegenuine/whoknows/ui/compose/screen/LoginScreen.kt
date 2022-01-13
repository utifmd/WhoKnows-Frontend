package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun LoginScreen(
    viewModel: UserViewModel = hiltViewModel()){

    val state = viewModel.state.value
    val formState = viewModel.createState

    Column {
        if (state.user != null){
            Text(text = state.user.fullName, style = MaterialTheme.typography.h6)
        }

        GeneralTextField(
            label = "Enter email",
            value = formState.email.text,
            onValueChange = formState.onUsernameChange
        )

        GeneralTextField(
            label = "Enter password",
            value = formState.password.text,
            onValueChange = formState.onPasswordChange
        )

        if (state.error.isNotBlank()) {
            Text(
                color = MaterialTheme.colors.error,
                text = ResourceState.DONT_EMPTY
            )
        }

        Button(
            enabled = formState.isValid.value,
            onClick = viewModel::signInUser) {
            Text(text = "Sign In")
        }
    }
}