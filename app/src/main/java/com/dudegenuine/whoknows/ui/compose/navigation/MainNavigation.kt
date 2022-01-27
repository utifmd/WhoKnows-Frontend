package com.dudegenuine.whoknows.ui.compose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.settingGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.summaryGraph
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalUnitApi
@Composable
fun MainNavigation(
    modifier: Modifier,
    controller: NavHostController,
    destination: String,
    viewModel: UserViewModel = hiltViewModel()) {

    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = controller){

        authNavGraph(
            router = controller,
            viewModel = viewModel,
        )

        homeNavGraph(
            modifier = modifier,
            router = controller,
            viewModel = viewModel,
        )

        summaryGraph(
            modifier = modifier,
            router = controller,
        )

        settingGraph(
            modifier = modifier,
            router = controller
        )
    }
}