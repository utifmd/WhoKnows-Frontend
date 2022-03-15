package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RoomStatedPreBoardingScreen(
    viewModel: RoomViewModel = hiltViewModel(),
    eventHome: IRoomEventHome,
    eventBoarding: IRoomEventBoarding) {
    val uiState = viewModel.uiState.observeAsState().value
    val state = viewModel.state

    val composeEvent: (Room.RoomState.BoardingQuiz) -> IRoomEventBoarding = { roomState ->
        object: IRoomEventBoarding {
            override fun onPrevPressed() { roomState.currentQuestionIdx -=1 }
            override fun onNextPressed() { roomState.currentQuestionIdx +=1 }
            override fun onDonePressed() { viewModel.onPreResult(roomState) }
        }
    }

    when {
        uiState is Room.RoomState.BoardingQuiz -> RoomBoardingScreen(
            state = uiState,
            viewModel = viewModel,
            onAction = eventBoarding::onAction,
            onPrevPressed = { composeEvent(uiState).onPrevPressed() },
            onNextPressed = { composeEvent(uiState).onNextPressed() },
            onDonePressed = { composeEvent(uiState).onDonePressed() }
        )
        uiState is Room.RoomState.BoardingResult -> ResultScreen(
            state = uiState,
            onDonePressed = { viewModel.onCloseBoarding() }
        )
        uiState is Room.RoomState.CurrentRoom -> RoomHomeScreen(
            event = eventHome
        )
        state.loading -> LoadingScreen()
        else -> ErrorScreen(message = state.error)
    }
}