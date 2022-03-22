package com.dudegenuine.whoknows.ui.compose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
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
fun MainNavigation(
    controller: NavHostController, // initialPassed: String,
    destination: String,
    viewModel: UserViewModel = hiltViewModel()) {

    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = controller){

        authNavGraph(controller, viewModel = viewModel)
        homeNavGraph(controller, viewModel = viewModel/*, initial = if (initialPassed == MESSAGE_INTENT *//*TIME_RUNNING*//*) Screen.Home.Discover else Screen.Home.Summary*/)
    }
}