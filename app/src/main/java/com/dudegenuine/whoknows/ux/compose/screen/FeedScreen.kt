package com.dudegenuine.whoknows.ux.compose.screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.component.misc.LazyStatePaging
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz.QuestItem
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomItem
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ux.theme.ColorBronze
import com.dudegenuine.whoknows.ux.theme.ColorGold
import com.dudegenuine.whoknows.ux.theme.ColorSilver
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun FeedScreen(
    modifier: Modifier = Modifier, props: IMainProps,
    onJoinButtonPressed: (() -> Unit)? = null, onSearchPressed: (() -> Unit)? = null) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val listState: LazyListState = rememberLazyListState()
    val swipeRefreshState = rememberSwipeRefreshState(false)
    val onRefresh: () -> Unit = {
        props.run {
            lazyPagingParticipants.refresh()
            lazyPagingRoomCensored.refresh()
            lazyPagingQuizzes.refresh()
        }
    }
    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = "Discovery",
                tails = Icons.Filled.Search,
                onTailPressed = onSearchPressed
            )
        },
        scaffoldState = scaffoldState) {
        SwipeRefresh(swipeRefreshState, onRefresh) {
            
            LazyColumn(modifier,
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp)) {
                item {
                    BodyRoom(modifier,
                        props.lazyPagingRoomCensored,
                        onJoinButtonPressed
                    )
                }
                item {
                    BodyQuiz(modifier.animateContentSize(
                        animationSpec = tween(600)),
                        props.lazyPagingQuizzes
                    )
                }
                item {
                    BodyParticipant(
                        modifier,
                        props.lazyPagingParticipants
                    )
                }
            }
        }
    }
}

@Composable
private fun BodyParticipant(
    modifier: Modifier, lazyParticipants: LazyPagingItems<User.Censored>?){
    if (lazyParticipants == null) return
    Body(modifier, "Most active participant${if (lazyParticipants.itemCount > 1) "\'s" else ""}",
        Icons.Filled.TrendingUp) {

        LazyRow(modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            item { LazyStatePaging(items = lazyParticipants, repeat = 3, height = 84.dp, horizontal = Arrangement.spacedBy(4.dp)) }
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
                        else -> ""}} ${item.fullName.ifBlank { stringResource(R.string.unknown) }}",
                    desc = item.username.padStart(1, '@'),
                    data = item.profileUrl
                )
            }
        }
    }
}

@Composable
private fun BodyRoom(
    modifier: Modifier,
    lazyRooms: LazyPagingItems<Room.Censored>?, onJoinButtonPressed: (() -> Unit)? = null){
    if (lazyRooms == null) return

    Body(modifier, "Most happening ${if (lazyRooms.itemCount > 1) "classes" else "class"}",
        Icons.Filled.Class/*, true*/) {
        val rowState = rememberLazyListState()

        LazyRow(modifier.padding(vertical = 6.dp),
            state = rowState,
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            item { LazyStatePaging(items = lazyRooms, repeat = 3, horizontal = Arrangement.spacedBy(4.dp)) }
            items(lazyRooms, { it.roomId }){ room ->
                room?.let {
                    RoomItem(
                        modifier.width(246.dp),
                        model = it,
                        onImpressed = {

                        }
                    )
                }
            }
        }

        Box(modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd){

            Button({ onJoinButtonPressed?.invoke() }, enabled = onJoinButtonPressed != null) {
                Text(stringResource(R.string.join_with_a_code), color = MaterialTheme.colors.onPrimary)
                Spacer(modifier.size(4.dp))
                Icon(Icons.Filled.ArrowForward, tint = MaterialTheme.colors.onPrimary, contentDescription = null)
            }
        }
    }
}

@Composable
private fun BodyQuiz(
    modifier: Modifier, lazyQuizzes: LazyPagingItems<Quiz.Complete>?){
    if (lazyQuizzes == null) return
    Body(modifier, "Random question${if (lazyQuizzes.itemCount > 1) "\'s" else ""}",
        Icons.Filled.Shuffle) {

        LazyRow(modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            item { LazyStatePaging(items = lazyQuizzes, repeat = 3, horizontal = Arrangement.spacedBy(4.dp)) }
            items(lazyQuizzes, { it.id }){ model ->
                model?.let { QuestItem(model = model) }
            }
        }
    }
}

@Composable
private fun Body(
    modifier: Modifier, title: String, icon: ImageVector,
    isTop: Boolean = false, content: @Composable () -> Unit){

    Column {
        if (!isTop) Spacer(modifier.size(ButtonDefaults.IconSize))

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {

            Icon(icon, contentDescription = null)
            Text(title)
        }

        content()
    }
}