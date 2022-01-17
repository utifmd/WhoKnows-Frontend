package com.dudegenuine.whoknows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoginScreen
import com.dudegenuine.whoknows.ui.compose.screen.MainScreen
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
class MainActivity: ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhoKnowsTheme {
                val controller = rememberNavController()
                val state = userViewModel.state

                if(state.loading){
                    LoadingScreen()
                }

                state.user?.let {
                    MainScreen(
                        navController = controller
                    )
                }

                if (state.error.isNotBlank()){
                    LoginScreen(
                        navHostController = controller
                    )
                }
                /*ProfileScreen() RegisterScreen() QuizCreatorScreen() RoomScreen()*/
            }
        }
    }
}