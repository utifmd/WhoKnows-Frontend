package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Sat, 05 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun RoomRoutedPreBoardingScreen(
    event: IRoomEventBoarding,
    viewModel: RoomViewModel = hiltViewModel(),
    onStopTimer: () -> Unit) {
    //val state = viewModel.state
    val uiState = viewModel.uiState.observeAsState().value

    val composeEvent: (Room.RoomState.BoardingQuiz) -> IRoomEventBoarding = { boardingState ->
        object : IRoomEventBoarding {
            override fun onPrevPressed()
                { boardingState.currentQuestionIdx -=1 }

            override fun onNextPressed()
                { boardingState.currentQuestionIdx +=1 }

            override fun onDonePressed() {
                onStopTimer()

                viewModel.onPreResult(boardingState)
            }
        }
    }

    uiState?.let { roomState ->
        when(roomState){
            is Room.RoomState.BoardingQuiz -> RoomBoardingScreen(
                state = roomState,
                viewModel = viewModel,
                onAction = event::onAction,
                onPrevPressed = { composeEvent(roomState).onPrevPressed() },
                onNextPressed = { composeEvent(roomState).onNextPressed() },
                onDonePressed = { composeEvent(roomState).onDonePressed() }
            )
            is Room.RoomState.BoardingResult -> RoomResultScreen(
                state = roomState,
                onDonePressed = event::onDoneResultPressed,
                // onBackPressed = {  },
                // onSharePressed = { viewModel.shareResult() },
            )
            else -> LoadingScreen()
        }
    }
}