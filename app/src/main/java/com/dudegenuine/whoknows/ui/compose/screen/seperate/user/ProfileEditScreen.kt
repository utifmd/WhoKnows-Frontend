package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.IProfileEvent.Companion.EMAIL
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.IProfileEvent.Companion.NAME
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.IProfileEvent.Companion.PASSWORD
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.IProfileEvent.Companion.PHONE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.IProfileEvent.Companion.USERNAME
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    fieldKey: String?,
    fieldValue: String?,
    viewModel: UserViewModel = hiltViewModel(),
    onSucceed: (User) -> Unit) {

    val field = remember {
        mutableStateOf( fieldValue ?: "" )
    }

    val onSubmitPressed: () -> Unit = {
        viewModel.state.user?.let { model ->
            val data = when(fieldKey) {
                NAME -> model.copy(
                    fullName = field.value)
                EMAIL -> model.copy(
                    email = field.value)
                PHONE -> model.copy(
                    phone = field.value)
                USERNAME -> model.copy(
                    username = field.value)
                PASSWORD -> model.copy(
                    password = field.value)
                else -> model
            }

            Log.d("ProfileEditScreen: ", "triggered")
            viewModel.patchUser(data, onSucceed)
        }
    }

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Profile edit",
                submitLabel = "Update",
                submitEnable = field.value.isNotBlank() && !viewModel.state.loading,
                submitLoading = viewModel.state.loading,
                onSubmitPressed = onSubmitPressed
            )
        }) { padding ->

        Box(
            modifier = modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            GeneralTextField(
                label = fieldKey ?: "No label",
                value = field.value,
                leads = Icons.Default.TextFields,
                tails = if(field.value.isNotBlank()) Icons.Default.Close else null,
                onValueChange = { field.value = it },
                onTailPressed = { field.value = "" }
            )
        }
    }

}