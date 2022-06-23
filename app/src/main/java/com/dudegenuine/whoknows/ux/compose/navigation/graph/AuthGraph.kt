package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.LoginScreen
import com.dudegenuine.whoknows.ux.compose.screen.RegisterScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.authNavGraph(props: IMainProps){
    navigation(
        route = Screen.Auth.route,
        startDestination = Screen.Auth.Login.route){
        val vmMain = props.viewModel as MainViewModel

        composable(
            route = Screen.Auth.Login.route) {
            val vmUser: UserViewModel = hiltViewModel()

            LoggingSubscriber(vmMain, vmUser)
            LoginScreen(
                viewModel = vmUser, auth = vmMain.auth) {
                props.router.navigate(Screen.Auth.Register.route)
            }
        }

        composable(
            route = Screen.Auth.Register.route) {
            val vmUser: UserViewModel = hiltViewModel()

            LoggingSubscriber(vmMain, vmUser)
            RegisterScreen(
                viewModel = vmUser, auth = vmMain.auth
            )
        }
    }
}