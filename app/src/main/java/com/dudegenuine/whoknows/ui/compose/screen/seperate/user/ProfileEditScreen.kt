package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

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
    onBackPressed: () -> Unit, onSucceed: (User.Complete) -> Unit) {
    var field by remember { mutableStateOf( fieldValue ?: "" ) }
    val onSubmitPressed: () -> Unit = {
        viewModel.onUpdateUser(fieldKey, field, onSucceed) }

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Profile edit",
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = onBackPressed,
                submitLabel = "Update",
                submitLoading = viewModel.state.loading,
                onSubmitPressed = onSubmitPressed,
                submitEnable = field.isNotBlank() &&
                        fieldValue != field &&
                        !viewModel.state.loading)}) { padding ->

        Column(modifier.fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            GeneralTextField(
                label = fieldKey ?: "No label",
                value = field,
                tails = if(field.isNotBlank()) Icons.Filled.Close else null,
                onValueChange = { field = it },
                onTailPressed = { field = "" })

            if (viewModel.state.error.isNotBlank())
                ErrorScreen(message = viewModel.state.error)
        }
    }

}