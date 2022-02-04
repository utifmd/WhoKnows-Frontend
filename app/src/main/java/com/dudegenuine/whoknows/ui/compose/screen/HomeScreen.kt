package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain

/**
 * Mon, 24 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    router: NavHostController,
    enabled: Boolean = false,
    content: @Composable () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()) {

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        content = { padding ->
            Box(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize()){

                content()
            }
        },
        bottomBar = {
            if (enabled) {
                GeneralBottomBar(
                    items = BottomDomain.list,
                    controller = router
                )
            }
        }
    )
}