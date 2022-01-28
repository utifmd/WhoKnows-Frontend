package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.CardFooter
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 27 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RoomDetail(
    modifier: Modifier = Modifier,
    isOwn: Boolean = false,
    roomViewModel: RoomViewModel = hiltViewModel(), /*participantViewModel: ParticipantViewModel = hiltViewModel(),*/
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    roomEventDetail: IRoomEventDetail) {

    val state = roomViewModel.state

    val conceal: () -> Unit = {
        if(scaffoldState.isRevealed)
            coroutineScope.launch { scaffoldState.conceal() }
    }

    val event = object: IRoomEventDetail {

        override fun onPublishRoomPressed() { conceal() }

        override fun onJoinRoomDirectlyPressed(room: Room) {
            val model = roomViewModel.formState.participantModel.copy(
                roomId = room.id,
                userId = room.userId,
                currentPage = "0",
                timeLeft = room.minute,
            )

            Log.d("JoinRoomDirectly", model.toString())

            /*participantViewModel
                .postParticipant(model)*/
        }

        override fun onNewRoomQuizPressed() =
            roomEventDetail.onNewRoomQuizPressed()

        override fun onParticipantItemPressed() =
            roomEventDetail.onParticipantItemPressed()
    }

    if (state.loading) {
        LoadingScreen(
            modifier = modifier)
    }

    state.room?.let { model ->
        BackdropScaffold(
            modifier = modifier.fillMaxSize(),
            scaffoldState = scaffoldState,

            appBar = {
                GeneralTopBar(
                    title = "${model.minute} minute\'s duration",
                    leads = Icons.Default.Timer,
                    light = false
                )
            },

            backLayerContent = {
                BackLayer(
                    modifier = modifier,
                    model = model,
                    isOwn = isOwn,
                    event = event
                )
            },

            frontLayerContent = {
                FrontLayer(
                    modifier = modifier,
                    model = model,
                    onParticipantPressed =
                        event::onParticipantItemPressed
                )
            }
        )
    }

    if (state.error.isNotBlank()) {
        ErrorScreen(
            modifier = modifier, message = state.error
        )
    }
}

@Composable
private fun BackLayer(
    modifier: Modifier,
    model: Room,
    isOwn: Boolean,
    event: IRoomEventDetail) {

    val enabled = !model.expired

    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (isOwn) {
            TextButton(
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = event::onPublishRoomPressed) {

                Text(
                    color = if (enabled) MaterialTheme.colors.surface
                        else MaterialTheme.colors.error,
                    text = "Publish this room"
                )
            }
        }

        TextButton(
            enabled = enabled,
            modifier = modifier.fillMaxWidth(),
            onClick = {
                if (isOwn) event.onNewRoomQuizPressed()
                else event.onJoinRoomDirectlyPressed(model) }) {

            Text(
                color = if (enabled) MaterialTheme.colors.surface
                    else MaterialTheme.colors.error,

                text = if (isOwn) "Add new question"
                    else "Join this room"
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun FrontLayer(
    modifier: Modifier,
    model: Room,
    onParticipantPressed: () -> Unit){

    Column(
        modifier = modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)) {


            Text(
                text = model.title,
                style = MaterialTheme.typography.h4)

            CardFooter(
                text = model.createdAt.toHttpDateString(),
                icon = Icons.Default.CalendarViewDay,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            )

            Text(
                text = model.description,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        }

        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {

            CardFooter(
                text = "${model.participants.size} " +
                        if (model.participants.size > 1) "Participant\'s" else "Participant",
                icon = Icons.Default.People,
            )

            CardFooter(
                text = "${model.questions.size} " +
                        if (model.questions.size > 1) "Questions\'s" else "Question",
                icon = Icons.Default.QuestionAnswer,
            )
        }

        if (model.participants.isNotEmpty()){
            Divider()
            LazyRow(
                modifier = modifier.fillMaxWidth()) {

                model.participants.forEach {
                    item {
                        ProfileCard(
                            modifier = Modifier.clickable(
                                onClick = onParticipantPressed
                            ),
                            name = it.userId.split("-")[0],
                            desc = timeAgo(it.createdAt),
                            url = "http://10.0.2.2:8080/files/785d4c17-cffa-4338-b551-37da6fa5272c"
                        )
                    }
                }
            }
        }
    }
}