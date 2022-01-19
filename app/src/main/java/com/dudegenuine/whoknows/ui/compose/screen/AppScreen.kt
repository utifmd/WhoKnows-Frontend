package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.dudegenuine.whoknows.ui.compose.route.HostNavigation
import com.dudegenuine.whoknows.ui.compose.route.Screen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel()) {
    val router = rememberNavController()

    userViewModel.apply {
        if (state.loading) {
            LoadingScreen()
        }

        state.user?.let {
            HostNavigation(
                modifier = modifier,
                destination = Screen.MainScreen.route,
                router = router
            )
        }

        if (state.error.isNotBlank()) {
            HostNavigation(
                modifier = modifier,
                destination = Screen.AuthScreen.route,
                router = router
            )
        }
    }
}