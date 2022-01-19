package com.dudegenuine.whoknows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain
import com.dudegenuine.whoknows.ui.compose.route.HostNavigation
import com.dudegenuine.whoknows.ui.compose.route.Screen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalCoilApi @ExperimentalMaterialApi @ExperimentalFoundationApi
class MainActivity: ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val state = userViewModel.state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setDefaultScreen
        setContent {
            WhoKnowsTheme {
                val router = rememberNavController()

                Scaffold(
                    content = { padding ->
                        /*if (state.loading) {
                            LoadingScreen()
                        }*/
                        state.user?.let { _ ->
                            HostNavigation(
                                modifier = Modifier.padding(padding),
                                destination = Screen.MainScreen.SummaryScreen.route,
                                router = router
                            )
                        }
                        if (state.error.isNotBlank()) {
                            HostNavigation(
                                modifier = Modifier.padding(padding),
                                destination = Screen.AuthScreen.LoginScreen.route,
                                router = router
                            )
                        }
                    },
                    bottomBar = {
                        state.user?.let { _ ->
                            GeneralBottomBar (
                                items = BottomDomain.list,
                                controller = router/*,
                                onItemClick = onBottomItemSelected*/
                            )
                        }
                    }
                )
            }
            /*WhoKnowsTheme {
                AppScreen()
            }*/
        }
    }

    /*private val setDefaultScreen: () -> Unit = {
        userViewModel.apply {
            getUser()
        }
    }*/
}