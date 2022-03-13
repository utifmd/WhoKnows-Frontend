package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import kotlinx.coroutines.FlowPreview

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun DiscoverScreen(
    router: NavHostController) {

    FeedScreen(
        onJoinButtonPressed = {
            router.navigate(Screen.Home.Discover.RoomFinder.route)
        },

        onNotificationPressed = {
            router.navigate(Screen.Home.Discover.Notification.route)
        }
    )
}