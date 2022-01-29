package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.ui.compose.component.GeneralCardView
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

    val toggle: () -> Unit = {
        coroutineScope.launch {
            if(scaffoldState.isRevealed)
                scaffoldState.conceal()
            else
                scaffoldState.reveal()
        }
    }

    val event = object: IRoomEventDetail {
        override fun onCloseRoomPressed() { toggle() }

        override fun onJoinRoomDirectlyPressed(room: Room) {
            toggle()

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

        override fun onNewRoomQuizPressed(roomId: String, owner: String) =
            roomEventDetail.onNewRoomQuizPressed(roomId, owner)

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
                    light = false,
                    title = "${model.minute} minute\'s duration",
                    leads = Icons.Default.Timer,
                    tails = Icons.Default.Menu,
                    onTailPressed = { toggle() }
                )
            },

            backLayerContent = {
                BackLayer(
                    model = model,
                    isOwn = isOwn,
                    event = event
                )
            },

            frontLayerContent = {
                FrontLayer(
                    model = model,
                    isOwn = isOwn,
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
    modifier: Modifier = Modifier,
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
                onClick = event::onCloseRoomPressed) {

                Text(
                    color = if (enabled) MaterialTheme.colors.surface
                    else MaterialTheme.colors.error,
                    text = "Close the room"
                )
            }
        }


        TextButton(
            enabled = enabled,
            modifier = modifier.fillMaxWidth(),
            onClick = {
                if (isOwn) event.onNewRoomQuizPressed(model.id, model.userId)
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
    modifier: Modifier = Modifier,
    model: Room,
    isOwn: Boolean,
    onParticipantPressed: () -> Unit) {

    Column(
        modifier = modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)) {

            Text(
                text = model.title,
                style = MaterialTheme.typography.h4)

            Spacer(
                modifier = Modifier.height(12.dp))

            CardFooter(
                text = model.createdAt.toHttpDateString(),
                icon = Icons.Default.CalendarToday,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            )

            Text(
                text = model.description,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        }

        CardFooter(
            modifier = modifier.padding(horizontal = 24.dp),
            text = "${model.participants.size} " +
                    if (model.participants.size > 1) "Participant\'s" else "Participant",
            icon = Icons.Default.People,
        )

        if (model.participants.isNotEmpty()){
            LazyRow(
                modifier = modifier.fillMaxWidth()) {

                model.participants.forEach {
                    item {
                        ProfileCard(
                            modifier = modifier.clickable(
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

        Spacer(modifier = Modifier.height(24.dp))

        CardFooter(
            modifier = modifier.padding(horizontal = 24.dp),
            text = "${model.questions.size} " +
                    if (model.questions.size > 1) "Questions\'s" else "Question",
            icon = Icons.Default.QuestionAnswer
        )

        if (isOwn && model.questions.isNotEmpty()){
            LazyColumn(
                modifier = modifier.fillMaxWidth()){
                repeat(model.questions.size) {
                    item {
                        GeneralCardView {
                            Box(
                                modifier = modifier.padding(12.dp)){

                                Text(
                                    text = /*"${it.plus(1)}. " + */model.questions[it].question,
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}