package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.User
import com.dudegenuine.model.User.Companion.KeyChanger.EMAIL
import com.dudegenuine.model.User.Companion.KeyChanger.NAME
import com.dudegenuine.model.User.Companion.KeyChanger.PHONE
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.state.UserState
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()) {
    val changerState = viewModel.changerState
    val field = remember { mutableStateOf( changerState.selectedFieldValue ?: "" ) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {

        GeneralTextField(
            label = changerState.fieldKey ?: "No label",
            value = field.value,
            leads = Icons.Default.TextFields,
            tails = if(field.value.isNotBlank()) Icons.Default.Check else null,
            onValueChange = { field.value = it },
            onTailPressed = {
                val model: User = when(changerState.fieldKey){
                    NAME -> changerState.model.copy(fullName = field.value)
                    EMAIL -> changerState.model.copy(email = field.value)
                    PHONE -> changerState.model.copy(phone = field.value)
                    else -> changerState.model
                }
                viewModel.onUiStateChange(UserState.CurrentState(model))
            },
        )
    }
}