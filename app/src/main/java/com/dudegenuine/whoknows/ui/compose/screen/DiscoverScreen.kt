package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
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
fun DiscoverScreen(props: IMainProps) {

    FeedScreen(
        props = props,
        onJoinButtonPressed = {
            props.router.navigate(Screen.Home.Discover.RoomFinder.route)
        },

        onNotificationPressed = {
            props.router.navigate(Screen.Home.Discover.Notification.route)
        }
    )
}