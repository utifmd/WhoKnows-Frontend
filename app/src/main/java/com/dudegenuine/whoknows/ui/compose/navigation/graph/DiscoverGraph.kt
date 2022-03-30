package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.NotificationScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 23 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@FlowPreview
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
fun NavGraphBuilder.discoverGraph(props: IMainProps){
    val notification = Screen.Home.Discover.Notification

    composable(
        route = notification.route,
        deepLinks = listOf(navDeepLink{ uriPattern = notification.uriPattern })){

        NotificationScreen(
            onBackPressed = props.router::popBackStack,
            onPressed = { roomId, userId ->
                props.router.navigate(
                    route = Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(roomId, userId)) },
            onDetailRoomPressed = {
                props.router.navigate(
                    route = Screen.Home.Discover.RoomDetail.routeWithArgs(it.roomId)
                )
            }
        )
    }

    composable(
        route = Screen.Home.Discover.RoomFinder.route){
        RoomFinderScreen(
            onBackPressed = props.router::popBackStack,
            onRoomSelected = { roomId ->
                props.router.navigate(
                    route = Screen.Home.Discover.RoomDetail.routeWithArgs(roomId/*, IRoomEvent.OWN_IS_FALSE*/))
            }
        )
    }

    composable(
        route = Screen.Home.Discover.RoomDetail.routeWithArgs(
            "{$ROOM_ID_SAVED_KEY}"/*, "{${IRoomEvent.ROOM_IS_OWN}}"*/)){// entry ->

        RoomDetail(
            onBackPressed = props.router::popBackStack,
            isOwn = false, //entry.arguments?.getString(IRoomEvent.ROOM_IS_OWN) == IRoomEvent.OWN_IS_TRUE,
            eventDetail = RoomEventDetail(props)
        )
    }
}