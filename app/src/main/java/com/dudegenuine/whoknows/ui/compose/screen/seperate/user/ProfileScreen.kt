package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.User
import com.dudegenuine.model.User.Companion.KeyChanger.EMAIL
import com.dudegenuine.model.User.Companion.KeyChanger.NAME
import com.dudegenuine.model.User.Companion.KeyChanger.PHONE
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralPicture
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.route.Screen
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.state.UserState
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalCoilApi
fun ProfileScreen(
    modifier: Modifier = Modifier,
    router: NavHostController,
    viewModel: UserViewModel = hiltViewModel()){
    val state = viewModel.state
    val uiState = viewModel.uiState.observeAsState()

    LaunchedEffect("currentUser"){
        viewModel.getUser()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = state.user?.username
                    ?: stringResource(R.string.profile_detail)
            )
        },
        content = {
            if (state.loading){
                LoadingScreen()
            }

            when(uiState.value){
                is UserState.ChangerState -> ProfileEditScreen(
                    router = router
                )
                else -> Body(
                    router = router,
                    viewModel = viewModel,
                )
            }

            if (state.error.isNotBlank()){
                ErrorScreen(message = state.error)
            }
        }
    )
}

@Composable
@ExperimentalCoilApi
private fun Body(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    router: NavHostController){
    val user: User = viewModel.state.user ?: return

    val scrollState = rememberScrollState()

    val onSignOutPressed: () -> Unit = {
        viewModel.signOutUser {
            router.navigate(Screen.AuthScreen.LoginScreen.route)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally){

        GeneralPicture(
            modifier = modifier,
            data = user.profileUrl
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))) {

            FieldTag(
                key = stringResource(R.string.full_name),
                value = user.fullName
                    .ifBlank { stringResource(R.string.not_set) },
                modifier = modifier,
                onEditPressed = {
                    viewModel.onUiStateChange(UserState.ChangerState(user, NAME))

                    /*changerState.onFullNameChange(user.fullName)*/
                }/*{
                    router.navigate(Screen.MainScreen.SettingScreen.FieldEditScreen
                        .withArgs(NAME, user.fullName))
                }*/
            )

            FieldTag(
                key = stringResource(R.string.phone_number),
                value = user.phone
                    .ifBlank { stringResource(R.string.not_set) },
                modifier = modifier,
                onEditPressed = {
                    viewModel.onUiStateChange(UserState.ChangerState(user, PHONE)) }/*{
                    router.navigate(Screen.MainScreen.SettingScreen.FieldEditScreen
                        .withArgs(PHONE, user.phone))
                }*/
            )

            FieldTag(
                key = stringResource(R.string.email),
                value = user.email,
                modifier = modifier,
                onEditPressed = {
                    viewModel.onUiStateChange(UserState.ChangerState(user, EMAIL)) }/*{
                    router.navigate(Screen.MainScreen.SettingScreen.FieldEditScreen
                        .withArgs(EMAIL, user.email))
                }*/
            )

            repeat(10){
                FieldTag(
                    key = stringResource(R.string.password),
                    value = user.password,
                    modifier = modifier,
                    editable = false
                )
            }
        }

        TextButton(
            onClick = onSignOutPressed) {
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
        modifier = modifier.fillMaxWidth(),
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
