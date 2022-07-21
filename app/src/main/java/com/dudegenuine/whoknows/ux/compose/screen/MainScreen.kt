package com.dudegenuine.whoknows.ux.compose.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.ux.compose.component.GeneralAlertDialog
import com.dudegenuine.whoknows.ux.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ux.compose.model.BottomDomain
import com.dudegenuine.whoknows.ux.compose.navigation.MainGraph
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
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
    val (badge, setBadge) = remember{
        mutableStateOf( props.viewModel.auth.user?.badge ?: 0)
    }
    val content: @Composable (PaddingValues) -> Unit = { padding ->
        Box(modifier.fillMaxSize().padding(padding)) {
            GeneralAlertDialog(viewModel = viewModel)

            MainGraph(props,
                if(viewModel.isLoggedIn) Screen.Home.route
                else Screen.Auth.route
            )
        }
    }
    val bottomBar: @Composable () -> Unit = {
        AnimatedVisibility(viewModel.auth.user != null,
            enter = fadeIn(), exit = fadeOut()){
            GeneralBottomBar(
                items = badge.let(BottomDomain.listItem),
                controller = props.router) {
                if (badge > 0 && it.name == BottomDomain.SUMMARY) setBadge(0)
            }
        }
    }
    WhoKnowsTheme {
        val scaffoldState = rememberScaffoldState()
        val snackHostState by remember {
            mutableStateOf(scaffoldState.snackbarHostState)
        }
        fun onNavigateTo(state: ScreenState.Navigate.To) =
            try { props.router.navigate(state.route, state.option) } catch (e: Exception){
                viewModel.onToast(e.localizedMessage ?: Resource.ILLEGAL_STATE_EXCEPTION)
        }
        fun onNavigateBack(state: ScreenState.Navigate.Back) = if(state.refresh) {
            props.router.previousBackStackEntry
                ?.savedStateHandle
                ?.set(Resource.KEY_REFRESH, true)
            props.router.popBackStack() } else
                props.router.popBackStack()

        val effect: suspend (ScreenState) -> Unit = { state ->
            when(state){
                is ScreenState.Toast -> Toast.makeText(
                    props.context, state.message, state.duration).show()
                is ScreenState.SnackBar -> snackHostState.showSnackbar(
                    state.message, state.label, state.duration)
                is ScreenState.Navigate.Back -> state.let(::onNavigateBack)
                is ScreenState.Navigate.To -> state.let(::onNavigateTo); else -> {}
            }
        }
        Scaffold(modifier,
            scaffoldState = scaffoldState,
            content = content,
            bottomBar = bottomBar
        )
        LaunchedEffect(viewModel.screenState){
            viewModel.screenState.collectLatest(effect)
        }
    }
}
