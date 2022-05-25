package com.dudegenuine.whoknows.ui.compose.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.dudegenuine.whoknows.ui.compose.component.GeneralAlertDialog
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain
import com.dudegenuine.whoknows.ui.compose.navigation.MainGraph
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
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
        val scaffoldState = rememberScaffoldState()
        val snackHostState = remember { mutableStateOf(scaffoldState.snackbarHostState) }

        LaunchedEffect(vmMain.snackMessage){
            vmMain.snackMessage.collectLatest(snackHostState.value::showSnackbar) }
        Scaffold(modifier,
            scaffoldState = scaffoldState,
            content = { padding ->
                Box(modifier
                    .fillMaxSize()
                    .padding(padding)) {
                    GeneralAlertDialog(vmMain)
                    when {
                        vmMain.state.loading -> LoadingScreen()
                        vmMain.isSignedIn -> MainGraph(props, Screen.Home.route)
                        else -> MainGraph(props, Screen.Auth.route)
                    }
                }
            },

            bottomBar = {
                if (vmMain.isSignedIn) {
                    GeneralBottomBar(
                        items = vmMain.badge.let(BottomDomain.listItem),
                        controller = router
                    )
                }
            }
        )
    }
}
