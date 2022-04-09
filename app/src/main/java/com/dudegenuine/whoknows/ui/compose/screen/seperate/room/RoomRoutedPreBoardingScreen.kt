package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Sat, 05 Feb 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomRoutedPreBoardingScreen(
    event: IRoomEventBoarding,
    viewModel: RoomViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.observeAsState().value
    val state = viewModel.state

    val composeEvent: (Room.State.BoardingQuiz) -> IRoomEventBoarding = { boardingState ->
        object : IRoomEventBoarding {
            override fun onPrevPressed() { boardingState.currentQuestionIdx -=1 }
            override fun onNextPressed() { boardingState.currentQuestionIdx +=1 }
            override fun onDonePressed() { viewModel.onPreResult(boardingState) }
        }
    }

    when {
        uiState is Room.State.BoardingQuiz -> RoomBoardingScreen(
            state = uiState,
            viewModel = viewModel,
            onAction = event::onAction,
            onPrevPressed = { composeEvent(uiState).onPrevPressed() },
            onNextPressed = { composeEvent(uiState).onNextPressed() },
            onDonePressed = { composeEvent(uiState).onDonePressed() }
        )
        uiState is Room.State.BoardingResult -> ResultScreen(
            state = uiState,
            onDonePressed = event::onDoneResultPressed
        )
        state.loading -> LoadingScreen()
        else -> ErrorScreen(message = state.error)
    }
}