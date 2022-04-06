package com.dudegenuine.whoknows.ui.compose.screen

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@FlowPreview
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    router: NavHostController = rememberNavController(),
    vmMain: ActivityViewModel = hiltViewModel()) {
    val props = object: IMainProps {
        override val context: Context = LocalContext.current
        override val router: NavHostController = router
        override val vmMain: IActivityViewModel = vmMain
        override val currentUserId: String = vmMain.userId
        override val vmRoom: RoomViewModel = hiltViewModel()
        val vmUser: UserViewModel = hiltViewModel()
        val vmQuiz: QuizViewModel = hiltViewModel()

        override val roomsPager: LazyPagingItems<Room.Censored> =
            vmRoom.rooms.collectAsLazyPagingItems()

        override var ownerRoomsPager: LazyPagingItems<Room.Complete> =
            /*if (vmMain.isSignedIn) vmRoom.roomsOwner.collectAsLazyPagingItems()
            else*/ emptyFlow<PagingData<Room.Complete>>().collectAsLazyPagingItems()

        override val participantsPager: LazyPagingItems<User.Censored> =
            vmUser.participants.collectAsLazyPagingItems()

        override val quizzesPager: LazyPagingItems<Quiz.Complete> =
            vmQuiz.questions.collectAsLazyPagingItems()
    }

    //var props by remember { mutableStateOf(initialProps) }

    WhoKnowsTheme {
        with(vmMain) {
            val snackHostState = scaffoldState.snackbarHostState

            Scaffold(modifier,
                scaffoldState = scaffoldState,
                content = { padding ->
                    Box(
                        modifier
                            .fillMaxSize()
                            .padding(padding)) {
                        GeneralAlertDialog(this@with)
                        when {
                            isSignedIn -> MainGraph(
                                props = props,
                                destination = Screen.Home.route
                            )
                            state.loading -> LoadingScreen()
                            else -> MainGraph(
                                props = props,
                                destination = Screen.Auth.route
                            )
                        }
                    }
                },

                bottomBar = {
                    if (isSignedIn) {
                        GeneralBottomBar(
                            items = badge.let(BottomDomain.listItem),
                            controller = router
                        )
                    }
                }
            )
            LaunchedEffect(snackMessage) {
                snackMessage.collectLatest(snackHostState::showSnackbar)
            }
        }
    }
}
