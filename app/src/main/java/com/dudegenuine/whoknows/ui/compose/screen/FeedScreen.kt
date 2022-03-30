package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.model.common.ViewUtil
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.LazyStatePaging
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomItem
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ui.theme.ColorBronze
import com.dudegenuine.whoknows.ui.theme.ColorGold
import com.dudegenuine.whoknows.ui.theme.ColorSilver
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun FeedScreen(props: IMainProps, modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    listState: LazyListState = rememberLazyListState(),
    onJoinButtonPressed: () -> Unit, onNotificationPressed: () -> Unit) {
    val vmMain = props.vmMain as ActivityViewModel

    val swipeRefreshState = rememberSwipeRefreshState(
        vmMain.pagingLoading(props.participantsPager) ||
            vmMain.pagingLoading(props.roomsPager) || vmMain.pagingLoading(props.quizzesPager))

    val onRefresh: () -> Unit = {
        props.participantsPager.refresh()
        props.roomsPager.refresh()
        props.quizzesPager.refresh()
    }

    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = "Discovery",
                tails = Icons.Filled.Notifications,
                tailsTint = if(vmMain.isNotify) MaterialTheme.colors.error else null,
                onTailPressed = {
                    vmMain.onTurnNotifierOn(false)
                    onNotificationPressed()
                },
            )
        },
        scaffoldState = scaffoldState) {
        SwipeRefresh(swipeRefreshState, onRefresh) {
            
            LazyColumn(modifier,
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp)) {

                item { BodyRoom(modifier, swipeRefreshState.isRefreshing, props.roomsPager, onJoinButtonPressed) }
                item { BodyQuiz(modifier, swipeRefreshState.isRefreshing, props.quizzesPager) }
                item { BodyParticipant(modifier, swipeRefreshState.isRefreshing, props.participantsPager) }

                item {
                    LazyStatePaging(
                        items = props.ownerRoomsPager,
                        repeat = 5, height = 130.dp, width = null)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
private fun BodyParticipant(
    modifier: Modifier, isRefreshing: Boolean, lazyParticipants: LazyPagingItems<User.Censored>){

    Body(modifier, "Most active participant${if (lazyParticipants.itemCount > 1) "\'s" else ""}",
        Icons.Filled.TrendingUp) {

        LazyRow(modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (isRefreshing) items(3) {
                LoadBoxScreen(height = 130.dp, width = 246.dp)
            }

            itemsIndexed(lazyParticipants) { index, item ->
                if (item != null) ProfileCard(modifier.fillMaxWidth(),
                    colorBorder = when (index) {
                        0 -> ColorGold
                        1 -> ColorSilver
                        2 -> ColorBronze
                        else -> null
                    },
                    name = "${when (index) {
                        0 -> "#1"
                        1 -> "#2"
                        2 -> "#3"
                        else -> ""}} ${item.fullName}",
                    desc = item.username.padStart(1, '@'),
                    data = item.profileUrl
                )
            }

            // item { LazyStatePaging(items = lazyParticipants, horizontal = Arrangement.spacedBy(4.dp), repeat = 3) }
        }
    }
}

@FlowPreview
@Composable
private fun BodyRoom(
    modifier: Modifier, isRefreshing: Boolean, lazyRooms: LazyPagingItems<Room.Censored>, onJoinButtonPressed: () -> Unit){
    Body(modifier, "Most happening class${if (lazyRooms.itemCount > 1) "es" else ""}",
        Icons.Filled.Class, true) {

        LazyRow(modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (isRefreshing) items(3) {
                LoadBoxScreen(height = 130.dp, width = 246.dp)
            }

            items(lazyRooms, { it.roomId }){ room ->
                room?.let {
                    RoomItem(
                        modifier.width(246.dp),
                        model = it
                    )
                }
            } //item { LazyStatePaging(items = lazyRooms, horizontal = Arrangement.spacedBy(4.dp), repeat = 3) }
        }

        Box(modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd){

            Button(onJoinButtonPressed, enabled = !isRefreshing) {
                Text("Join with a code")
                Spacer(modifier.size(4.dp))
                Icon(Icons.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun BodyQuiz(
    modifier: Modifier, isRefreshing: Boolean, lazyQuizzes: LazyPagingItems<Quiz.Complete>){
    Body(modifier, "Random question${if (lazyQuizzes.itemCount > 1) "\'s" else ""}",
        Icons.Filled.Shuffle) {

        LazyRow(modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (isRefreshing) items(3) {
                LoadBoxScreen(height = 130.dp, width = 246.dp)
            }
            items(lazyQuizzes, { it.id }) { item ->
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
                            withStyle(
                                SpanStyle(
                                fontWeight = FontWeight.SemiBold)
                            ) {

                                append(item.user?.fullName ?: "unknown")
                            }

                            withStyle(
                                SpanStyle(MaterialTheme.colors.onSurface.copy(0.5f),
                                fontStyle = FontStyle.Italic, fontSize = 11.sp)
                            ) {

                                append(" @${item.user?.username ?: "@unknown"}")
                                append(" ${ViewUtil.timeAgo(item.createdAt)}")
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
            } //item { LazyStatePaging(items = lazyQuizzes, horizontal = Arrangement.spacedBy(4.dp), repeat = 3) }
        }
    }
}

@Composable
private fun Body(
    modifier: Modifier, title: String, icon: ImageVector,
    isTop: Boolean = false, content: @Composable () -> Unit){

    Column {
        if (!isTop) Spacer(modifier.size(ButtonDefaults.IconSize))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Icon(icon, contentDescription = null)
            Text(title)
        }

        content()
    }
}

/*LazyColumn(
    contentPadding = PaddingValues(12.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)) {

    item {
        LazyRow(modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {

            items(lazyQuizzes) { item ->
                item?.let { QuestItem(it) }
            }

            //item { LazyStatePaging(items = lazyQuizzes, horizontal = Arrangement.spacedBy(4.dp), repeat = 3) }
        }
    }
}*/