package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.LazyStatePaging
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomItem
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.vm.participant.ParticipantViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun FeedScreen(modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scrollState: ScrollState = rememberScrollState(),
    vmQuiz: QuizViewModel = hiltViewModel(),
    vmNotification: NotificationViewModel = hiltViewModel(),
    vmParticipant: ParticipantViewModel = hiltViewModel(),
    vmRoom: RoomViewModel = hiltViewModel(), onJoinButtonPressed: () -> Unit, onNotificationPressed: () -> Unit) {
    val lazyParticipants = vmParticipant.participants.collectAsLazyPagingItems()
    val lazyRooms = vmRoom.rooms.collectAsLazyPagingItems()
    val lazyQuizzes = vmQuiz.questions.collectAsLazyPagingItems()
    val isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = "Discovery",
                tails = Icons.Filled.Notifications,
                tailsTint = if(vmNotification.badge.isNotBlank() && vmNotification.badge != "0")
                    MaterialTheme.colors.error else null,
                onTailPressed = onNotificationPressed,
            )
        },
        scaffoldState = scaffoldState){

        SwipeRefresh(swipeRefreshState, onRefresh = {
            lazyParticipants.refresh()
            lazyQuizzes.refresh()
            lazyRooms.refresh()
        }) {
            Box(modifier.verticalScroll(scrollState)){

                Column(modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)){

                    Header(modifier, "Most happening question", Icons.Filled.TrendingUp, true)
                    BodyQuiz(modifier, lazyQuizzes)

                    Header(modifier,"Most popular classes", Icons.Filled.Class)
                    BodyRoom(modifier, lazyRooms, onJoinButtonPressed)

                    Header(modifier,"Most actively participant", Icons.Filled.People)
                    BodyParticipant(modifier, lazyParticipants)
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun BodyParticipant(
    modifier: Modifier, lazyParticipants: LazyPagingItems<Participant>){

    LazyRow(modifier.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {

        items(lazyParticipants) { item ->
            if (item != null) ProfileCard(
                modifier.fillMaxWidth(),
                name = item.user?.fullName ?: stringResource(R.string.unknown),
                desc = (item.user?.username ?: stringResource(R.string.unknown)).padStart(1, '@'),
                data = item.user?.profileUrl ?: ""
            )
        }

        item { LazyStatePaging(items = lazyParticipants, times = 3) }
    }
}

@FlowPreview
@Composable
private fun BodyRoom(
    modifier: Modifier, lazyRooms: LazyPagingItems<Room>, onJoinButtonPressed: () -> Unit){

    LazyRow(modifier.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {

        items(lazyRooms){ room ->
            if (room != null) RoomItem(modifier.width(246.dp),
                state = room, censored = true){

            }
        }

        item { LazyStatePaging(items = lazyRooms, times = 3) }
    }

    Box(modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd){

        Button(onJoinButtonPressed) {
            Text("Join with a code")
            Spacer(modifier.size(4.dp))
            Icon(Icons.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun BodyQuiz(
    modifier: Modifier, lazyQuizzes: LazyPagingItems<Quiz>){

    LazyRow(modifier.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {

        items(lazyQuizzes) { item ->
            item?.let {
                Row(
                    modifier
                        .width(246.dp)
                        .background(
                            color = MaterialTheme.colors.onSurface.copy(
                                alpha = if (MaterialTheme.colors.isLight) 0.04f else 0.06f
                            ),
                            shape = MaterialTheme.shapes.small
                        )) {
                    val text = buildAnnotatedString {
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.SemiBold)) {

                            append(item.user?.fullName ?: "unknown")
                        }

                        withStyle(SpanStyle(MaterialTheme.colors.onSurface.copy(0.5f),
                            fontStyle = FontStyle.Italic, fontSize = 11.sp)) {

                            append(" @${item.user?.username ?: "@unknown"}")
                            append(" ${timeAgo(item.createdAt)}")
                        }

                        append("\n")
                        append(item.question)
                    }

                    Text(text, //style = MaterialTheme.typography.subtitle1, maxLines = 5, overflow = TextOverflow.Ellipsis,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 24.dp,
                                horizontal = 16.dp
                            )
                    )
                }
            }
        }

        item { LazyStatePaging(items = lazyQuizzes, times = 3) }
    }
}

@Composable
private fun Header(modifier: Modifier, title: String, icon: ImageVector, isTop: Boolean = false){
    if (!isTop) Spacer(modifier.size(ButtonDefaults.IconSize))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {

        Icon(icon, contentDescription = null)
        Text(title)
    }
}