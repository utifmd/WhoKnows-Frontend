package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
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