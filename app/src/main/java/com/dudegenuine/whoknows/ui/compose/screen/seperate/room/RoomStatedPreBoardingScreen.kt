package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RoomStatedPreBoardingScreen(
    //modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
    eventHome: IRoomEventHome,
    eventBoarding: IRoomEventBoarding,
    onStopTimer: () -> Unit) {
    val uiState = viewModel.uiState.observeAsState().value

    val composeEvent: (Room.RoomState.BoardingQuiz) -> IRoomEventBoarding = { roomState ->
        object: IRoomEventBoarding {
            override fun onPrevPressed()
                { roomState.currentQuestionIdx -=1 }

            override fun onNextPressed()
                { roomState.currentQuestionIdx +=1 }

            override fun onDonePressed() {
                onStopTimer()

                viewModel.onPreResult(roomState)
            }
        }
    }

    when(uiState){
        is Room.RoomState.BoardingQuiz -> RoomBoardingScreen(
            state = uiState,
            viewModel = viewModel,
            onAction = eventBoarding::onAction,
            onPrevPressed = { composeEvent(uiState).onPrevPressed() },
            onNextPressed = { composeEvent(uiState).onNextPressed() },
            onDonePressed = { composeEvent(uiState).onDonePressed() }
        )
        is Room.RoomState.BoardingResult -> RoomResultScreen(
            state = uiState,
            onDonePressed = { viewModel.onCloseResult() },
            // onBackPressed = {  },
            // onSharePressed = { viewModel.shareResult() },
        )
        else -> RoomHomeScreen(
            event = eventHome
        )
    }
}