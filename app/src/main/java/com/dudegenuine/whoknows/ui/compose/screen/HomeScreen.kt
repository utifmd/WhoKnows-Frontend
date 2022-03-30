package com.dudegenuine.whoknows.ui.compose.screen

/*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

*/
/**
 * Mon, 24 Jan 2022
 * WhoKnows by utifmd
 **//*

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@FlowPreview
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    router: NavHostController,
    enabled: Boolean = false,
    vmMain: ActivityViewModel = hiltViewModel(),
    content: @Composable () -> Unit) {
    val snackHostState = vmMain.scaffoldState.snackbarHostState

    LaunchedEffect(snackHostState) {
        vmMain.snackMessage.collectLatest {
            snackHostState.showSnackbar(it)
        }
    }
    
    Scaffold(modifier,
        scaffoldState = vmMain.scaffoldState,
        content = { padding ->
            Box(modifier
                .fillMaxSize()
                .padding(padding)) { content() }},

        bottomBar = {
            if (enabled) {
                GeneralBottomBar(
                    items = vmMain.badge.let(BottomDomain.listItem),
                    controller = router
                )
            }
        }
    )
}*/
