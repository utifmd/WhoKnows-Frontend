package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadBoxScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Wed, 29 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@Composable
fun RoomHomeScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: RoomViewModel = hiltViewModel(),
    event: IRoomEventHome) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Created class", //"Recently class\'s",
            )
        },
        modifier = modifier,
        scaffoldState = scaffoldState) {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {

            stickyHeader {
                Header(
                    modifier = modifier,
                    onNewClassPressed = event::onNewClassPressed,
                    onJoinWithACodePressed = event::onJoinRoomWithACodePressed,
                )
            }

            if (state.loading) items(5) {
                LoadBoxScreen(height = 130.dp)
            }

            state.rooms?.forEach { room ->
                item {
                    RoomItem(
                        state = room){

                        event.onRoomItemSelected(room.id)
                    }
                }
            }
        }

        if(state.error.isNotBlank()) ErrorScreen(
            modifier = modifier, message = state.error, isDanger = false
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    onNewClassPressed: () -> Unit,
    onJoinWithACodePressed: () -> Unit){

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()) {

        Button(
            modifier = modifier.weight(1f),
            onClick = onNewClassPressed) {
            Text(text = stringResource(R.string.new_class))
        }

        OutlinedButton(
            modifier = modifier.weight(1f),
            onClick = onJoinWithACodePressed) {
            Text(text = stringResource(R.string.join_with_a_code))
        }
    }
}