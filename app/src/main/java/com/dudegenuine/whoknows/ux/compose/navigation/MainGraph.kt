package com.dudegenuine.whoknows.ux.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.dudegenuine.whoknows.ux.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ux.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun MainGraph(
    props: IMainProps, destination: String) {
    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = props.router) {

        authNavGraph(props)
        homeNavGraph(props)
    }
}