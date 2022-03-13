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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel

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
    viewModel: NotificationViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()) {
    val badge: Int = if (viewModel.badge.isNotBlank()) viewModel.badge.toInt()
        else viewModel.state.notifications?.count { !it.seen } ?: 0

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
                    items = BottomDomain.listItem(badge),
                    controller = router
                )
            }
        }
    )
}