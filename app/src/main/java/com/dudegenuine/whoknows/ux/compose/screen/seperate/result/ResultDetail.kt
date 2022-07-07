package com.dudegenuine.whoknows.ux.compose.screen.seperate.result

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.ResultScreen
import com.dudegenuine.whoknows.ux.vm.result.ResultViewModel

/**
 * Wed, 09 Mar 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                ResultScreen(modifier,
                    state = result,
                    onDonePressed = viewModel::onDonePressed
                )
            }
            if (state.error.isNotBlank())
                ErrorScreen(message = state.error)
        }
    )
}