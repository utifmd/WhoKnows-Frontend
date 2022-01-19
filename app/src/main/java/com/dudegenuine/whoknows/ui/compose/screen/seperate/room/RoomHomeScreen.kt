package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.route.Screen
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Wed, 29 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun RoomHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(), router: NavHostController) {
    val state = viewModel.state

    val onNewClassPressed: () -> Unit = {
        router.navigate(Screen.MainScreen.DiscoverScreen.RoomCreatorScreen.route)
    }

    val onJoinWithACodePressed: () -> Unit = {
        router.navigate(Screen.MainScreen.DiscoverScreen.RoomFinderScreen.route)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            GeneralTopBar(
                title = "Recently class", //"Recently class\'s",
            )
        },
        content = {
            Column {
                Header(
                    onNewClassPressed = onNewClassPressed,
                    onJoinWithACodePressed = onJoinWithACodePressed,
                )

                Body(state)
            }
        }
    )
}

@Composable
private fun Body(
    state: ResourceState,
    modifier: Modifier = Modifier){

    if (state.loading){
        LoadingScreen()
    }
    if(state.error.isNotBlank()){
        ErrorScreen(modifier = modifier, message = state.error, isDanger = false)

    } else{
        LazyColumn(
            modifier = modifier.fillMaxWidth()) {
            state.rooms?.forEach {
                item { RoomItem(modifier = modifier, state = it) }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    onNewClassPressed: () -> Unit,
    onJoinWithACodePressed: () -> Unit){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)) {

        Button(
            modifier = modifier.weight(1f),
            onClick = onNewClassPressed) {
            Text(text = stringResource(R.string.new_class))
        }

        Spacer(modifier = modifier.width(16.dp))
        OutlinedButton(
            modifier = modifier.weight(1f),
            onClick = onJoinWithACodePressed) {
            Text(text = stringResource(R.string.join_with_a_code))
        }
    }
}