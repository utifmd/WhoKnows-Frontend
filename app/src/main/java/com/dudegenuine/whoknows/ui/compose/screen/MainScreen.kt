package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/

@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
fun MainScreen(
    router: NavHostController){

    Scaffold(
        content = {
            MainNavigation(
                router = router
            )
        },
        bottomBar = {
            GeneralBottomBar (
                items = BottomDomain.list,
                controller = router,
                /*onItemClick = { router.navigate(it.route) }*/
            )
        }
    )
}

@Composable
fun MainNavigation(router: NavHostController) {
    TODO("Not yet implemented")
}
