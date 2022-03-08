package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.NotificationScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventDetail

/**
 * Wed, 23 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
fun NavGraphBuilder.discoverGraph(
    router: NavHostController){

    composable(
        route = Screen.Home.Discover.Notification.route){
        NotificationScreen(
            onBackPressed = router::popBackStack
        )
    }

    composable(
        route = Screen.Home.Discover.RoomFinder.route){
        RoomFinderScreen(
            onBackPressed = router::popBackStack,
            onRoomSelected = { roomId ->
                router.navigate(
                    Screen.Home.Discover.RoomDetail.withArgs(roomId, IRoomEvent.OWN_IS_FALSE))
            }
        )
    }

    composable(
        route = Screen.Home.Discover.RoomDetail.withArgs("{${IRoomEvent.ROOM_ID_SAVED_KEY}}", "{${IRoomEvent.ROOM_IS_OWN}}")){ entry ->

        RoomDetail(
            onBackPressed = router::popBackStack,
            isOwn = entry.arguments?.getString(IRoomEvent.ROOM_IS_OWN) == IRoomEvent.OWN_IS_TRUE,
            eventRouter = RoomEventDetail(router = router)
        )
    }
}