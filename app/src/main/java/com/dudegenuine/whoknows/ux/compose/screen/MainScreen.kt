package com.dudegenuine.whoknows.ux.compose.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ux.compose.component.GeneralAlertDialog
import com.dudegenuine.whoknows.ux.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ux.compose.model.BottomDomain
import com.dudegenuine.whoknows.ux.compose.navigation.MainGraph
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import com.dudegenuine.whoknows.ux.theme.WhoKnowsTheme
import com.dudegenuine.whoknows.ux.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ux.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ux.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun MainScreen(
    modifier: Modifier = Modifier, intent: Intent,
    router: NavHostController = rememberNavController(),
    vmMain: ActivityViewModel = hiltViewModel(),
    vmRoom: RoomViewModel = hiltViewModel(),
    vmUser: UserViewModel = hiltViewModel(),
    vmQuiz: QuizViewModel = hiltViewModel()) {

    val props = object: IMainProps {
        override val state: ResourceState get() = vmMain.state
        override val store: ResourceState.Store get() = vmMain.storeState

        override val context: Context = LocalContext.current
        override val router: NavHostController = router
        override val vmMain: IActivityViewModel = vmMain
        override val intent: Intent = intent

        override val lazyPagingRooms: LazyPagingItems<Room.Censored> =
            vmRoom.rooms.collectAsLazyPagingItems()

        override var lazyPagingOwnerRooms: LazyPagingItems<Room.Complete> =
            emptyFlow<PagingData<Room.Complete>>().collectAsLazyPagingItems()

        override val lazyPagingParticipants: LazyPagingItems<User.Censored> =
            vmUser.participants.collectAsLazyPagingItems()

        override val lazyPagingQuizzes: LazyPagingItems<Quiz.Complete> =
            vmQuiz.questions.collectAsLazyPagingItems()
    }

    WhoKnowsTheme {
        val context = LocalContext.current
        val scaffoldState = rememberScaffoldState()
        val snackHostState by remember { mutableStateOf(scaffoldState.snackbarHostState) }

        LaunchedEffect(vmMain.screenState) {
            vmMain.screenState.collectLatest { state -> when(state){
                is ScreenState.Toast ->
                    with(state) { Toast.makeText(context, message, duration).show() }

                is ScreenState.SnackBar ->
                    with(state) { snackHostState.showSnackbar(message, label, duration) }

                is ScreenState.Navigate.Back -> props.router.popBackStack()
                is ScreenState.Navigate.To -> props.router
                    .navigate(state.route, state.option)

                else -> {}}
            }
        }

        Scaffold(modifier,
            scaffoldState = scaffoldState,
            content = { padding ->
                Box(
                    modifier
                        .fillMaxSize()
                        .padding(padding)) {

                    GeneralAlertDialog(vmMain)

                    when {
                        vmMain.state.loading -> LoadingScreen()
                        vmMain.isLoggedIn -> MainGraph(props, Screen.Home.route)
                        else -> MainGraph(props, Screen.Auth.route)
                    }
                }
            },

            bottomBar = {
                if (vmMain.isLoggedIn) {
                    GeneralBottomBar(
                        items = vmMain.state.badge.let(BottomDomain.listItem),
                        controller = router){
                        props.state.badge.let {
                            if (it > 0) it.minus(1)
                            vmMain.onStateChange(ResourceState(badge = it))
                        }
                    }
                }
            }
        )
    }
}
