package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.MainNavigation
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()) {

    val router = rememberNavController()
    val state = viewModel.state

    WhoKnowsTheme {
        HomeScreen(
            modifier = modifier.fillMaxSize(),
            router = router,
            enabled = state.user != null,
            content = {
                if (state.loading) {
                    LoadingScreen()
                }

                if (state.user != null) {
                    MainNavigation(
                        modifier = modifier.padding(bottom = 30.dp),
                        controller = router,
                        destination = Screen.Home.route)
                }

                if (state.error.isNotBlank()) {
                    MainNavigation(
                        modifier = modifier,
                        viewModel = viewModel,
                        controller = router,
                        destination = Screen.Auth.route)
                }
            }
        )
    }
}