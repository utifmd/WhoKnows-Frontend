package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.MainNavigation
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

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
    modifier: Modifier = Modifier, //initialPassed: String,
    vmMain: BaseViewModel = hiltViewModel(),
    vmUser: UserViewModel = hiltViewModel()) {
    val router = rememberNavController()

    LaunchedEffect(vmMain.snackMessage) {
        with (vmMain.scaffoldState.snackbarHostState) {
            vmMain.snackMessage.collectLatest { showSnackbar(it) }
        }
    }

    WhoKnowsTheme {
        HomeScreen(modifier.fillMaxSize(),
            vmMain = vmMain as ActivityViewModel,
            router = router,
            enabled = vmUser.state.user != null,
            content = {

                if (vmUser.state.loading) LoadingScreen()

                if (vmUser.state.user != null) {
                    MainNavigation(
                        controller = router,
                        destination = Screen.Home.route //, initialPassed = initialPassed
                    )
                }

                if (vmUser.state.error.isNotBlank()) {
                    MainNavigation(
                        viewModel = vmUser,
                        controller = router,
                        destination = Screen.Auth.route //, initialPassed = initialPassed
                    )
                }
            }
        )
    }
}
