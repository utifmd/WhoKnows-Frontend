package com.dudegenuine.whoknows.ui.compose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.settingGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.summaryGraph
import com.dudegenuine.whoknows.ui.service.MessagingService.Companion.MESSAGE_INTENT
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun MainNavigation(
    controller: NavHostController,
    initialPassed: String,
    destination: String,
    viewModel: UserViewModel = hiltViewModel()) {

    val context = LocalContext.current.applicationContext
    /*val activity = context.findActivity()
    val routed = activity?.intent?.getStringExtra(MainActivity.INITIAL_DATA_KEY)*/

    //Log.d( "MainNavigation", "$routed")

    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = controller){

        authNavGraph(
            router = controller,
            viewModel = viewModel,
        )

        homeNavGraph(
            context = context,
            router = controller,
            viewModel = viewModel,
            initial = if (initialPassed == MESSAGE_INTENT /*TIME_RUNNING*/)
                Screen.Home.Discover else Screen.Home.Summary,
        )

        summaryGraph(
            context = context,
            router = controller,
        )

        settingGraph(
            router = controller
        )
    }
}