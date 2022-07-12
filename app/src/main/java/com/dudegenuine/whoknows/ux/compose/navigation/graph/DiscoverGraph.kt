package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.SearchScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.search.SearchViewModel

/**
 * Wed, 23 Feb 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.discoverGraph(props: IMainProps){
    val vmMain = props.viewModel as MainViewModel

    composable(
        route = Screen.Home.Discover.RoomFinder.route){
        val vmRoom: RoomViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmRoom)
        RoomFinderScreen(
            viewModel = vmRoom/*,
            onBackPressed = props.router::popBackStack,
            onRoomSelected = { roomId ->
                props.router.navigate(
                    route = Screen.Home.Discover.RoomDetail.routeWithArgs(roomId*//*, IRoomEvent.OWN_IS_FALSE*//*))
            }*/
        )
    }

    composable(
        route = Screen.Home.Discover.SearchScreen.route){
        val viewModel: SearchViewModel = hiltViewModel()
        LoggingSubscriber(parent = props.viewModel, child = viewModel)
        SearchScreen(viewModel = viewModel)
    }

    composable(
        route = Screen.Home.Discover.RoomDetail.routeWithArgs("{$ROOM_ID_SAVED_KEY}"/*, "{${IRoomEvent.ROOM_IS_OWN}}"*/)){ // entry ->
        val vmRoom: RoomViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmRoom)
        RoomDetail(viewModel = vmRoom, onBackPressed = vmRoom::onBackPressed)
    }
}