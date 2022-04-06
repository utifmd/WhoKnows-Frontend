package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.LoginScreen
import com.dudegenuine.whoknows.ui.compose.screen.RegisterScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@FlowPreview
fun NavGraphBuilder.authNavGraph(props: IMainProps){
    navigation(
        route = Screen.Auth.route,
        startDestination = Screen.Auth.Login.route){
        composable(
            route = Screen.Auth.Login.route) {
            //val viewModel: UserViewModel = hiltViewModel()

            LoginScreen(
                /*viewModel = viewModel (props.vmUser as UserViewModel)*/){
                //props.vmMain.onShowSnackBar("Ini dari login forms")
                props.router.navigate(Screen.Auth.Register.route)
            }
        }

        composable(
            route = Screen.Auth.Register.route) {
            RegisterScreen(
                /*viewModel = (props.vmUser as UserViewModel)*/
            )
        }
    }
}