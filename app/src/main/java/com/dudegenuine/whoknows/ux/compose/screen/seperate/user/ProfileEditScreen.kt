package com.dudegenuine.whoknows.ux.compose.screen.seperate.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import com.dudegenuine.whoknows.ux.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    currentState: User.Complete?,
    viewModel: UserViewModel = hiltViewModel()) {
    val (field, setField) = remember{ mutableStateOf(EMPTY_STRING) }
    fun updateChanges() = currentState?.let(viewModel::onUpdateUser)

    Scaffold(topBar = {
        GeneralTopBar(
            title = stringResource(R.string.porifile_edit),
            submitLabel = stringResource(R.string.update),
            leads = Icons.Filled.ArrowBack,
            submitLoading = viewModel.state.loading,
            onLeadsPressed = viewModel::onBackPressed,
            onSubmitPressed = ::updateChanges,
            submitEnable = field.isNotBlank() && !viewModel.state.loading)}) { padding ->

        Column(
            modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)) {

            GeneralTextField(
                label = viewModel.fieldKey,
                value = field,
                trail = if(field.isNotBlank()) Icons.Filled.Close else null,
                onValueChange = setField,
                onTailPressed = { setField(EMPTY_STRING) }
            )
            if (viewModel.state.error.isNotBlank())
                ErrorScreen(message = viewModel.state.error)
        }
    }
}