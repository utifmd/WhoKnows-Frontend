package com.dudegenuine.whoknows.ux.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.paging.compose.collectAsLazyPagingItems
import com.dudegenuine.whoknows.ux.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ux.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun MainGraph(
    props: IMainProps, destination: String) {
    val vmRoom: RoomViewModel = hiltViewModel()
    val userId = (props.vmMain as ActivityViewModel).currentUserId
    val delegateProps = props.apply {
        if(userId.isNotBlank())
            lazyPagingOwnerRooms = vmRoom.roomsOwnerDirectly(userId).collectAsLazyPagingItems()
    }

    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = props.router) {

        authNavGraph(props)
        homeNavGraph(delegateProps)
    }
}