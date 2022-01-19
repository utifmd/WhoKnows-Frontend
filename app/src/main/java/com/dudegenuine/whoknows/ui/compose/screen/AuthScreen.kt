package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun AuthScreen(
    router: NavHostController,
    viewModel: UserViewModel = hiltViewModel()) {
    val state = viewModel.state


}

