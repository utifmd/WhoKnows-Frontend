package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralAlertDialog
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain
import com.dudegenuine.whoknows.ui.compose.navigation.MainNavigation
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
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
    vmMain: ActivityViewModel = hiltViewModel(),
    vmUser: UserViewModel = hiltViewModel()) {
    val router = rememberNavController()
    val snackHostState = vmMain.scaffoldState.snackbarHostState

    WhoKnowsTheme {
        Scaffold(modifier,
            scaffoldState = vmMain.scaffoldState,
            content = { padding ->
                Box(modifier.fillMaxSize().padding(padding)) {
                    if (vmUser.state.user != null) {
                        MainNavigation(
                            vmMain = vmMain,
                            vmUser = vmUser,
                            controller = router,
                            destination = Screen.Home.route
                        )
                    }
                    if (vmUser.state.error.isNotBlank()) {
                        MainNavigation(
                            vmMain = vmMain,
                            vmUser = vmUser,
                            controller = router,
                            destination = Screen.Auth.route
                        )
                    }
                    if (vmMain.state.loading) LoadingScreen()
                }
            },

            bottomBar = {
                if (vmUser.state.user != null) {
                    GeneralBottomBar(
                        items = vmMain.badge.let(BottomDomain.listItem),
                        controller = router
                    )
                }
            }
        )

        GeneralAlertDialog(vmMain)
        LaunchedEffect(vmMain.snackMessage) {
            vmMain.snackMessage.collectLatest {
                snackHostState.showSnackbar(it)
            }
        }
    }
}
