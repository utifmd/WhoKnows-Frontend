package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
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
fun RoomStatedPreBoardingScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
    eventHome: IRoomEventHome,
    eventBoarding: IRoomEventBoarding,
    onStopTimer: () -> Unit) {
    val uiState = viewModel.uiState.observeAsState().value

    val composeEvent: (RoomState.BoardingQuiz) -> IRoomEventBoarding = { roomState ->
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

    uiState?.let { roomState ->
        when(roomState){
            is RoomState.BoardingQuiz -> RoomBoardingScreen(
                state = roomState,
                onAction = eventBoarding::onAction,
                onPrevPressed = { composeEvent(roomState).onPrevPressed() },
                onNextPressed = { composeEvent(roomState).onNextPressed() },
                onDonePressed = { composeEvent(roomState).onDonePressed() },
                onBackPressed = eventBoarding::onBackPressed
            )
            is RoomState.BoardingResult -> RoomResultScreen(
                state = roomState,
                onDonePressed = { viewModel.onCloseResult() },
                // onBackPressed = {  },
                // onSharePressed = { viewModel.shareResult() },
            )
            else -> RoomHomeScreen(
                event = eventHome
            )
        }
    }
}