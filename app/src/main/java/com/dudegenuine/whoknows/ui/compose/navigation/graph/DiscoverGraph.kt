package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.dudegenuine.whoknows.ui.compose.component.misc.DialogSubscriber
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.NotificationScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventDetail
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

/**
 * Wed, 23 Feb 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.discoverGraph(props: IMainProps){
    val notification = Screen.Home.Discover.Notification
    val vmMain = props.vmMain as ActivityViewModel
    val isSignedIn = (props.vmMain as ActivityViewModel).isSignedIn

    composable(
        route = notification.route,
        deepLinks = if (isSignedIn) listOf(
            navDeepLink{ uriPattern = notification.uriPattern }) else emptyList()) {
        val vmNotifier: NotificationViewModel = hiltViewModel()
        val vmUser: UserViewModel = hiltViewModel()


        DialogSubscriber(vmMain, vmUser)
        NotificationScreen(props,
            vmNotifier = vmNotifier,
            vmUser = vmUser,
            onBackPressed = {
                props.router.navigate(Screen.Home.Discover.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
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
        val vmRoom: RoomViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmRoom)
        RoomFinderScreen(
            viewModel = vmRoom,
            onBackPressed = props.router::popBackStack,
            onRoomSelected = { roomId ->
                props.router.navigate(
                    route = Screen.Home.Discover.RoomDetail.routeWithArgs(roomId/*, IRoomEvent.OWN_IS_FALSE*/))
            }
        )
    }

    composable(
        route = Screen.Home.Discover.RoomDetail.routeWithArgs(
            "{$ROOM_ID_SAVED_KEY}"/*, "{${IRoomEvent.ROOM_IS_OWN}}"*/)){ // entry ->
        val vmRoom: RoomViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmRoom)
        RoomDetail(
            viewModel = vmRoom,
            onBackPressed = props.router::popBackStack,
            isOwn = false, //entry.arguments?.getString(IRoomEvent.ROOM_IS_OWN) == IRoomEvent.OWN_IS_TRUE,
            eventDetail = RoomEventDetail(props, vmRoom)
        )
    }
}