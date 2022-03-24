package com.dudegenuine.whoknows.ui.compose.screen.seperate.result

import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.ResultScreen
import com.dudegenuine.whoknows.ui.vm.result.ResultViewModel

/**
 * Wed, 09 Mar 2022
 * WhoKnows by utifmd
 **/
@Composable
fun ResultDetail(
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = hiltViewModel(),
    onBackPressed: () -> Unit) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Result detail",
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = onBackPressed
            )
        },
        content = {
            if (state.loading) LoadingScreen()

            state.result?.let { result ->
                val resultState = Room.State.BoardingResult(
                    result.user?.fullName ?: "Unknown", result) //"Joined by: ${result.user?.fullName ?: "Unknown"}", result)

                ResultScreen(modifier, state = resultState)
            }
            if (state.error.isNotBlank())
                ErrorScreen(message = stringResource(R.string.join_on_progress))
        }
    )
}