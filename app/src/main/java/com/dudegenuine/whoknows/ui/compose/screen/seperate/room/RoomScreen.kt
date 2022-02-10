package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RoomScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
    event: IRoomEventHome) {
    val uiState = viewModel.uiState.observeAsState().value
    /*LaunchedEffect(Dispatchers.IO) { viewModel.getOwnerRoom() }*/

    uiState?.let { roomState ->
        when(roomState){
            is RoomState.BoardingQuiz -> RoomBoardingScreen(
                // resourceState = resourceState,
                state = roomState,
                onAction = { _, _ ->  },
                onPrevPressed = { roomState.currentQuestionIdx -=1 },
                onNextPressed = { roomState.currentQuestionIdx +=1 },
                onDonePressed = { viewModel.onPreResult(roomState) },
                onBackPressed = { }
            )
            is RoomState.BoardingResult -> RoomResultScreen(
                state = roomState,
                onDonePressed = { viewModel.onCloseResult() },
                // onBackPressed = {  },
                // onSharePressed = { viewModel.shareResult() },
            )
            else -> RoomHomeScreen(
                event = event
            )
        }
    }
}