package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@FlowPreview
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    router: NavHostController) {

    FeedScreen(modifier,
        onJoinButtonPressed = {
            router.navigate(Screen.Home.Discover.RoomFinder.route)
        },

        onNotificationPressed = {
            router.navigate(Screen.Home.Discover.Notification.route)
        }
    )
}