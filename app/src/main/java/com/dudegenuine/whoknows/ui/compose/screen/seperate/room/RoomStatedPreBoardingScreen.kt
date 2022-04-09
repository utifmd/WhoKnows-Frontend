package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun RoomStatedPreBoardingScreen(
    props: IMainProps,
    eventHome: IRoomEventHome,
    eventBoarding: IRoomEventBoarding,
    viewModel: RoomViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.observeAsState().value
    val state = viewModel.state

    val composeEvent: (Room.State.BoardingQuiz) -> IRoomEventBoarding = { roomState ->
        object: IRoomEventBoarding {
            override fun onPrevPressed() { roomState.currentQuestionIdx -=1 }
            override fun onNextPressed() { roomState.currentQuestionIdx +=1 }
            override fun onDonePressed() { viewModel.onPreResult(roomState) }}
    }
    when (uiState) {
        is Room.State.BoardingQuiz -> RoomBoardingScreen(
            state = uiState,
            viewModel = viewModel,
            onAction = eventBoarding::onAction,
            onPrevPressed = { composeEvent(uiState).onPrevPressed() },
            onNextPressed = { composeEvent(uiState).onNextPressed() },
            onDonePressed = { composeEvent(uiState).onDonePressed() }
        )
        is Room.State.BoardingResult -> ResultScreen(
            state = uiState, onDonePressed = viewModel::onCloseBoarding
        )
        is Room.State.BoardingPrepare -> LoadingScreen()
        is Room.State.CurrentRoom -> RoomHomeScreen(eventHome, props = props)
        else -> ErrorScreen(message = state.error)
    }
}