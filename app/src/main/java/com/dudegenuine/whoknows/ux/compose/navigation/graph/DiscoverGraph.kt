package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.notification.NotificationScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ux.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ux.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Wed, 23 Feb 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.discoverGraph(props: IMainProps){
    val notification = Screen.Home.Discover.Notification
    val vmMain = props.vmMain as ActivityViewModel
    val isLoggedIn = (props.vmMain as ActivityViewModel).isLoggedIn

    composable(
        route = notification.route,
        deepLinks = if (isLoggedIn) listOf(
            navDeepLink{ uriPattern = notification.uriPattern }) else emptyList()) {
        val vmNotifier: NotificationViewModel = hiltViewModel()
        val vmUser: UserViewModel = hiltViewModel()


        LoggingSubscriber(vmMain, vmUser)
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

        LoggingSubscriber(vmMain, vmRoom)
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

        LoggingSubscriber(vmMain, vmRoom)
        RoomDetail(
            viewModel = vmRoom/*,
            onBackPressed = props.router::popBackStack,
            //isOwn = false, //entry.arguments?.getString(IRoomEvent.ROOM_IS_OWN) == IRoomEvent.OWN_IS_TRUE,
            eventDetail = RoomEventDetail(props, vmRoom)*/
        )
    }
}