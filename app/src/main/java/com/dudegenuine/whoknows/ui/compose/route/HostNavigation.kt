package com.dudegenuine.whoknows.ui.compose.route

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.*
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomHomeScreen

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
fun HostNavigation(
    modifier: Modifier,
    router: NavHostController, destination: String) {

    NavHost(
        navController = router,
        startDestination = destination){

        composable(
            route = Screen.AuthScreen.LoginScreen.route) {
            LoginScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.AuthScreen.RegisScreen.route) {
            RegisterScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.MainScreen.SummaryScreen.route) {
            SummaryScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.MainScreen.DiscoverScreen.route) {
            DiscoverScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.MainScreen.DiscoverScreen.RoomHomeScreen.route) {
            RoomHomeScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.MainScreen.DiscoverScreen.RoomCreatorScreen.route) {
            RoomCreatorScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.MainScreen.DiscoverScreen.RoomFinderScreen.route) {
            RoomFinderScreen(
                modifier = modifier,
                router = router
            )
        }
        composable(
            route = Screen.MainScreen.SettingScreen.route) {
            SettingScreen(
                modifier = modifier,
                router = router
            )
        }
        /*composable( *//*"profile?fieldId={fieldId}"*//**//*"/{single_edit}/?={field_name}"*//*
            route = Screen.MainScreen.SettingScreen.FieldEditScreen.route + "/{fieldKey}/{fieldValue}",
            arguments = listOf(
                navArgument("fieldKey"){ defaultValue = "No result." },
                navArgument("fieldValue"){ defaultValue = "No result." })) { entry ->

            ProfileEditScreen(
                modifier = modifier,
                router = router,
                fieldKey = entry.arguments?.getString("fieldKey"),
                fieldValue = entry.arguments?.getString("fieldValue")
            )

            *//*ErrorScreen(
                message = entry.arguments?.getString("fieldValue") ?: "Not you" //?: (": " + entry.arguments?.getString("fieldValue"))
            )*//*

        }*/
    }
}