package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.MainNavigation
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@FlowPreview
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    initialPassed: String,
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
                        controller = router,
                        destination = Screen.Home.route,
                        initialPassed = initialPassed
                    )
                }

                if (state.error.isNotBlank()) {
                    MainNavigation(
                        viewModel = viewModel,
                        controller = router,
                        destination = Screen.Auth.route,
                        initialPassed = initialPassed
                    )
                }
            }
        )
    }
}
