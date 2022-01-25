package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.LoginScreen
import com.dudegenuine.whoknows.ui.compose.screen.RegisterScreen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.authNavGraph(
    router: NavHostController,
    viewModel: UserViewModel){

    navigation(
        route = Screen.Auth.route,
        startDestination = Screen.Auth.Login.route){

        composable(
            route = Screen.Auth.Login.route) {
            LoginScreen(
                viewModel = viewModel){

                router.navigate(Screen.Auth.Register.route)
            }
        }

        composable(
            route = Screen.Auth.Register.route) {
            RegisterScreen()
        }
    }
}