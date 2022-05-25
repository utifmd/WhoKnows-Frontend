package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dudegenuine.whoknows.ui.compose.component.misc.DialogSubscriber
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.LoginScreen
import com.dudegenuine.whoknows.ui.compose.screen.RegisterScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.authNavGraph(props: IMainProps){
    navigation(
        route = Screen.Auth.route,
        startDestination = Screen.Auth.Login.route){
        val vmMain = props.vmMain as ActivityViewModel

        composable(
            route = Screen.Auth.Login.route) {
            val vmUser: UserViewModel = hiltViewModel()

            DialogSubscriber(vmMain, vmUser)
            LoginScreen(
                viewModel = vmUser) {
                props.router.navigate(Screen.Auth.Register.route)
            }
        }

        composable(
            route = Screen.Auth.Register.route) {
            val vmUser: UserViewModel = hiltViewModel()

            DialogSubscriber(vmMain, vmUser)
            RegisterScreen(
                viewModel = vmUser
            )
        }
    }
}