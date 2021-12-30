package com.dudegenuine.whoknows.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuizCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomBoardingScreen
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
fun RoomScreen(viewModel: RoomViewModel = hiltViewModel()) {
    val TAG = "RoomScreen"
    // val resourceState = viewModel.resourceState.value
    val uiState = viewModel.uiState.observeAsState().value

    uiState?.let { roomState ->
        when(roomState){
            is RoomState.CurrentRoom -> {
                // TODO: listing users room's
            }
            is RoomState.CreateQuizzes -> {
                QuizCreatorScreen()
            }
            is RoomState.BoardingQuiz -> {
                RoomBoardingScreen(
                    // resourceState = resourceState,
                    state = roomState,
                    onAction = { id, _ ->
                        Log.d(TAG, "RoomScreen: onAction $id")
                    },
                    // onBackPressed = {  },
                    onPrevPressed = { roomState.currentQuestionIdx -=1 },
                    onNextPressed = { roomState.currentQuestionIdx +=1 },
                    onDonePressed = { viewModel.computeResult(roomState) }
                )
            }
            is RoomState.BoardingResult -> {
                Log.d(TAG, "RoomScreen: BoardingQuiz")
                Text(text = "Room Result Screen")
            }
        }
    }
}