package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralPicture
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalCoilApi
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    onSignOutPressed: () -> Unit){
    val state = viewModel.state /*val uiState = viewModel.uiState.observeAsState()*/

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = state.user?.username
                    ?: stringResource(R.string.profile_detail)
            )
        },
        content = { padding ->
            if (state.loading){
                LoadingScreen(modifier = modifier.padding(padding))
            }

            Body(
                modifier = modifier.padding(padding),
                viewModel = viewModel,
                onSignOutPressed = onSignOutPressed
            )

            if (state.error.isNotBlank()){
                ErrorScreen(
                    message = state.error, modifier = modifier.padding(padding)
                )
            }

            /*when(uiState.value){
                is UserState.ChangerState -> ProfileEditScreen(
                    modifier = modifier.padding(padding)
                )
                else ->
            }*/
        }
    )
}

@Composable
@ExperimentalCoilApi
private fun Body(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    onSignOutPressed: () -> Unit){
    val user: User = viewModel.state.user ?: return

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally){

        Spacer(modifier = Modifier.height(12.dp))
        GeneralPicture(
            modifier = modifier,
            data = user.profileUrl
        )

        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))) {

            FieldTag(
                key = stringResource(R.string.full_name),
                value = user.fullName
                    .ifBlank { stringResource(R.string.not_set) },
                modifier = modifier
            )

            FieldTag(
                key = stringResource(R.string.phone_number),
                value = user.phone
                    .ifBlank { stringResource(R.string.not_set) },
                modifier = modifier
            )

            FieldTag(
                key = stringResource(R.string.username),
                value = user.username,
                modifier = modifier
            )

            FieldTag(
                key = stringResource(R.string.email),
                value = user.email,
                modifier = modifier,
                editable = false
            )

            FieldTag(
                key = stringResource(R.string.password),
                value = user.password,
                modifier = modifier,
                editable = false
            )
        }

        TextButton(
            onClick = onSignOutPressed) {
            Text(text = stringResource(R.string.sign_out), color = MaterialTheme.colors.error)
        }
    }
}