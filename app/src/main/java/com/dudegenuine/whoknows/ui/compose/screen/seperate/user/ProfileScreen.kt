package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralPicture
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
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
    viewModel: UserViewModel = hiltViewModel()){
    val uiState = viewModel.state

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = uiState.user?.username ?: stringResource(R.string.profile_detail)
            )
        },
        content = {
            if (uiState.loading){
                LoadingScreen()
            }

            uiState.user?.let {
                Body(
                    uiState = it,
                    onLogoutPressed = viewModel::singOutUser
                )
            }

            if (uiState.error.isNotBlank()){
                ErrorScreen(message = uiState.error)
            }
        }
    )
}

@Composable
@ExperimentalCoilApi
private fun Body(
    modifier: Modifier = Modifier,
    uiState: User,
    onLogoutPressed: () -> Unit){
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .scrollable(orientation = Orientation.Vertical, state = scrollState)
            .fillMaxSize()
            .padding(8.dp)) {

        GeneralPicture(
            modifier = modifier,
            data = uiState.profileUrl)

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)) {

            FieldTag(
                key = stringResource(R.string.full_name),
                value = uiState.fullName
                    .ifBlank { stringResource(R.string.not_set) },
                modifier = modifier)

            FieldTag(
                key = stringResource(R.string.phone_number),
                value = uiState.phone
                    .ifBlank { stringResource(R.string.not_set) },
                modifier = modifier)

            FieldTag(
                key = stringResource(R.string.email),
                value = uiState.email,
                modifier = modifier
            )

            FieldTag(
                key = stringResource(R.string.password),
                value = uiState.password,
                modifier = modifier,
                editable = false
            )
        }

        TextButton(
            onClick = onLogoutPressed) {
            Text(text = stringResource(R.string.sign_out), color = MaterialTheme.colors.error)
        }
    }
}

@Composable
private fun FieldTag(
    modifier: Modifier = Modifier,
    key: String,
    value: String,
    editable: Boolean = true,
    onEditPressed: (() -> Unit)? = null){

    val onEditClick: () -> Unit = {
        if(onEditPressed != null) onEditPressed()
    }

    Row(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(
            modifier = modifier.padding(
                horizontal = 12.dp),
            text = key)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {

            TextButton(
                enabled = editable,
                onClick = onEditClick) {

                Text(text = value)
                if(editable){
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
