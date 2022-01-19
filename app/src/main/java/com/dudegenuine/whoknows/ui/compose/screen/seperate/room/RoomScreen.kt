package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
fun RoomScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(), router: NavHostController) { /*val resourceState = viewModel.resourceState.value*/
    val uiState = viewModel.uiState.observeAsState().value

    uiState?.let { roomState ->
        when(roomState){
            is RoomState.BoardingQuiz -> {
                RoomBoardingScreen(
                    // resourceState = resourceState,
                    state = roomState,
                    onAction = { id, _ ->  },
                    // onBackPressed = {  },
                    onPrevPressed = { roomState.currentQuestionIdx -=1 },
                    onNextPressed = { roomState.currentQuestionIdx +=1 },
                    onDonePressed = { viewModel.computeResult(roomState) }
                )
            }
            is RoomState.BoardingResult -> {
                RoomResultScreen(
                    state = roomState,
                    // onBackPressed = {  },
                    // onSharePressed = { viewModel.shareResult() },
                    onDonePressed = {
                        viewModel.closeResult()
                    }
                )
            }
            else -> {
                viewModel.getUser()

                RoomHomeScreen(router = router)
            }
        }
    }
}