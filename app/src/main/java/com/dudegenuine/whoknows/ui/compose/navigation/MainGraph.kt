package com.dudegenuine.whoknows.ui.compose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@FlowPreview
@Composable
fun MainGraph(
    props: IMainProps, destination: String) {
    val vmRoom: RoomViewModel = hiltViewModel()
    val userId = (props.vmMain as ActivityViewModel).userId
    val delegateProps = props.apply {
        ownerRoomsPager = vmRoom.roomsOwnerDirectly(userId).collectAsLazyPagingItems()
    }

    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = props.router) {

        authNavGraph(props)
        homeNavGraph(delegateProps)
    }
}