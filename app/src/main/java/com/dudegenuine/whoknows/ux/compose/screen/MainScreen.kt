package com.dudegenuine.whoknows.ux.compose.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.dudegenuine.whoknows.ux.compose.component.GeneralAlertDialog
import com.dudegenuine.whoknows.ux.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ux.compose.model.BottomDomain
import com.dudegenuine.whoknows.ux.compose.navigation.MainGraph
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import com.dudegenuine.whoknows.ux.compose.state.room.FlowParameter
import com.dudegenuine.whoknows.ux.theme.WhoKnowsTheme
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun MainScreen(
    modifier: Modifier = Modifier, props: IMainProps) {
    val viewModel = props.viewModel as MainViewModel
    WhoKnowsTheme {
        val scaffoldState = rememberScaffoldState()
        val snackHostState by remember{ mutableStateOf(scaffoldState.snackbarHostState) }
        var badge by remember/*(props.viewModel.auth.user?.notifications?.size)*/{
            mutableStateOf(props.viewModel.auth.user?.notifications?.size ?: 0)
        }
        Scaffold(modifier,
            scaffoldState = scaffoldState,
            content = { padding ->
                Box(
                    modifier
                        .fillMaxSize()
                        .padding(padding)) {
                    GeneralAlertDialog(modifier, viewModel)
                    when {
                        viewModel.state.loading -> LoadingScreen()
                        viewModel.isLoggedInByPrefs -> MainGraph(props, Screen.Home.route)
                        else -> MainGraph(props, Screen.Auth.route)
                    }
                }
            },
            bottomBar = {
                if (viewModel.auth.user != null) {
                    GeneralBottomBar(
                        items = badge.let(BottomDomain.listItem),
                        controller = props.router) {
                        if (badge > 0 && it.name == BottomDomain.SUMMARY) badge = 0
                    }
                }
            }
        )
        LaunchedEffect(viewModel.auth.user){
            viewModel.auth.user?.id?.let { viewModel.onRoomCompleteParameterChange(FlowParameter.RoomComplete(it)) }
        }
        LaunchedEffect(viewModel.screenState){
            viewModel.screenState.collectLatest{ state ->
                when(state){
                    is ScreenState.Toast -> with(state) { Toast.makeText(props.context, message, duration).show() }
                    is ScreenState.SnackBar -> with(state) { snackHostState.showSnackbar(message, label, duration) }
                    is ScreenState.Navigate.Back -> props.router.popBackStack()
                    is ScreenState.Navigate.To -> props.router.navigate(state.route, state.option)
                    else -> {}
                }
            }
        }
    }
}
