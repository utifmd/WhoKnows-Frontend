package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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
    viewModel: RoomViewModel = hiltViewModel(), router: NavHostController) {
    val resourceState = viewModel.resourceState.value

    val onNewClassPressed: () -> Unit = {
        router.navigate(Screen.DiscoverScreen.RoomCreatorScreen.route)
    }

    val onJoinWithACodePressed: () -> Unit = {
        router.navigate(Screen.DiscoverScreen.RoomFinderScreen.route)
    } /*router.navigate(Screen.CoinDetailScreen.route + "/${coin.id}")*/

    Scaffold(
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
                Body(resourceState)
            }
        }
    )
}

@Composable
private fun Body(
    resourceState: ResourceState,
    modifier: Modifier = Modifier){

    if (resourceState.loading){
        LoadingScreen()
    }
    if(resourceState.error.isNotBlank()){
        ErrorScreen(modifier = modifier, message = resourceState.error, isDanger = false)

    } else{
        LazyColumn(
            modifier = modifier.fillMaxWidth()) {
            resourceState.rooms?.forEach {
                item {
                    Column(
                        modifier = modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp)) {

                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
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
            Text(
                text = "New class"
            )}
        Spacer(modifier = modifier.width(16.dp))

        OutlinedButton(
            modifier = modifier.weight(1f),
            onClick = onJoinWithACodePressed) {
            Text(text = "Join with a code")
        }
    }
}