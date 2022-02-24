package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.NotificationScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen

/**
 * Wed, 23 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
fun NavGraphBuilder.discoverGraph(
    router: NavHostController){

    composable(
        route = Screen.Home.Discover.RoomFinder.route){
        RoomFinderScreen(
            onRoomSelected = {},
            onBackPressed = router::popBackStack
        )
    }

    composable(
        route = Screen.Home.Discover.Notification.route){
        NotificationScreen(
            onBackPressed = router::popBackStack
        )
    }
}