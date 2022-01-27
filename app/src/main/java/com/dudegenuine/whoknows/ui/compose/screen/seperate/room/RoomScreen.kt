package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel
import kotlinx.coroutines.Dispatchers

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RoomScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
    event: IRoomEvent) {
    val uiState = viewModel.uiState.observeAsState().value

    LaunchedEffect(Dispatchers.IO) { viewModel.getOwnerRoom() }

    uiState?.let { roomState ->
        when(roomState){
            is RoomState.BoardingQuiz -> RoomBoardingScreen(
                // resourceState = resourceState,
                // onBackPressed = {  },
                state = roomState,
                onAction = { _, _ ->  },
                onPrevPressed = { roomState.currentQuestionIdx -=1 },
                onNextPressed = { roomState.currentQuestionIdx +=1 },
                onDonePressed = { viewModel.computeResult(roomState) })
            is RoomState.BoardingResult -> RoomResultScreen(
                // onBackPressed = {  },
                // onSharePressed = { viewModel.shareResult() },
                state = roomState,
                onDonePressed = { viewModel.closeResult() })
            else -> RoomHomeScreen(
                modifier = modifier,
                onNewClassPressed = event::onNewClassPressed,
                onJoinWithACodePressed = event::onJoinWithACodePressed
            )
        }
    }
}