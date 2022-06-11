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
        onJoinButtonPressed = if(props.viewModel.auth.user != null) {
            { props.router.navigate(Screen.Home.Discover.RoomFinder.route) }
        } else null,
        onSearchPressed = if(props.viewModel.auth.user != null) {
            { props.router.navigate(Screen.Home.Discover.SearchScreen.route) }
        } else null
    )
}