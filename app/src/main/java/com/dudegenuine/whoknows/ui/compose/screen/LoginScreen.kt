package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
    val formState = viewModel.userState.value

    Column {
        GeneralTextField(
            value = formState.email.value,
            errorMessage = state.error,
            onValueChange = formState.onUsernameChange
        )
        GeneralTextField(
            value = formState.password.value,
            errorMessage = state.error,
            onValueChange = formState.onPasswordChange
        )

        if (state.error.isNotBlank()) {
            Text(
                color = MaterialTheme.colors.error,
                text = ResourceState.DONT_EMPTY
            ) // error message
        }

        Button(
            enabled = formState.isValid.value,
            onClick = viewModel::signInUser) {
            Text(text = "Sign In")
        }
    }
}

@Composable
private fun GeneralTextField(
    value: String,
    errorMessage: String,
    onValueChange: (text: String) -> Unit){
    // val field = fieldState.value
    Column {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isError = errorMessage.isNotBlank()
        )
//        if (errorMessage.isNotBlank()) {
//            Text(
//                text = ResourceState.DONT_EMPTY
//            ) // error message
//        }
    }
}