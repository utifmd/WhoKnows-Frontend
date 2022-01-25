package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileEditScreen

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.settingGraph(
    modifier: Modifier = Modifier){
    composable(Screen.Home.Setting.ProfileEditor.route.plus("/{}")){

        ProfileEditScreen()
    }
}