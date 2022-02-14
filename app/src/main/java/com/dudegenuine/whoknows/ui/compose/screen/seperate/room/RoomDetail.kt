package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Result
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
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
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RoomDetail(
    modifier: Modifier = Modifier,
    isOwn: Boolean = false,
    viewModel: RoomViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState
        (BackdropValue.Concealed),
    eventRouter: IRoomEventDetail,
    onLaunchTimer: (Double) -> Unit) {

    val state = viewModel.state

    val toggle: () -> Unit = {
        coroutineScope.launch {
            if(scaffoldState.isRevealed)
                scaffoldState.conceal()
            else
                scaffoldState.reveal()
        }
    }

    val evenCompose = object: IRoomEventDetail {

        override fun onShareRoomPressed(roomId: String)
            { viewModel.onClipboardPressed(roomId) }

        override fun onCloseRoomPressed(room: Room)
            { viewModel.expireRoom(room) { toggle() } }

        override fun onJoinRoomDirectlyPressed(room: Room) {
            val asSecond = room.minute.toFloat() * 60

            toggle()
            eventRouter.onBoardingRoomPressed(room.id)

            onLaunchTimer(asSecond.toDouble())
        }

        override fun onDeleteRoomPressed(roomId: String) {
            viewModel.deleteRoom(roomId)
                { eventRouter.onDeleteRoomSucceed(it) }
        }
    }

    if (state.loading) LoadingScreen(modifier)

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
                    evenCompose = evenCompose,
                    evenRouter = eventRouter
                )
            },

            frontLayerContent = {
                FrontLayer(
                    contentModifier = modifier,
                    model = model,
                    isOwn = isOwn,
                    onProfileSelected =
                        eventRouter::onParticipantItemPressed,
                    onQuestionPressed =
                        eventRouter::onQuestionItemPressed,
                    onResultSelected = evenCompose::onResultPressed
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
    evenCompose: IRoomEventDetail,
    evenRouter: IRoomEventDetail) {
    val enabled = !model.expired

    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {

        TextButton(
            enabled = enabled,
            modifier = modifier.fillMaxWidth(),
            onClick = {
                if (isOwn) evenRouter.onNewRoomQuizPressed(model.id, model.userId)
                else evenCompose.onJoinRoomDirectlyPressed(model ) }) {

            Text(
                color = if (enabled) MaterialTheme.colors.onPrimary else Color.LightGray,

                text = if (isOwn) "Add New Question"
                    else "Join This Room"
            )
        }

        if (isOwn) {
            TextButton(
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = { evenCompose.onShareRoomPressed(model.id) }) {

                Text(
                    color = if (enabled) MaterialTheme.colors.onPrimary else Color.LightGray,
                    text = "Copy Invitation Code"
                )
            }

            TextButton(
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = { evenCompose.onCloseRoomPressed(model) }) {

                Text(
                    color = if (enabled) MaterialTheme.colors.onPrimary else Color.LightGray,
                    text = "Close This Room"
                )
            }

            TextButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { evenCompose.onDeleteRoomPressed(model.id) }) {

                Text(
                    color = MaterialTheme.colors.onPrimary,
                    text = "Delete Permanently"
                )
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
private fun FrontLayer(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    model: Room,
    isOwn: Boolean,
    onQuestionPressed: (String) -> Unit,
    onProfileSelected: (String) -> Unit,
    onResultSelected: (Result) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = contentModifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {

        Spacer(
            modifier = modifier.height(36.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)) {

            Text(
                text = model.title,
                style = MaterialTheme.typography.h4)

            CardFooter(
                text = model.createdAt.toHttpDateString(),
                icon = Icons.Default.CalendarToday,
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(model.description, style = MaterialTheme.typography.body2)
            }
        }
        
        Spacer(modifier.size(ButtonDefaults.IconSize))
        CardFooter(
            modifier = modifier.padding(horizontal = 24.dp),
            text = "${model.participants.size} " +
                    if (model.participants.size > 1)
                        "Participant\'s" else "Participant",
            icon = Icons.Default.People,
        )

        if (model.participants.isNotEmpty()){
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp
                    )) {

                model.participants.forEach { participant ->
                    item {
                        ProfileCard(
                            modifier = modifier.clickable(
                                onClick = {
                                    /*if (isOwn) onResultSelected(participant.result)
                                    else*/ onProfileSelected(participant.userId)
                                }
                            ),
                            name = participant.user?.fullName ?: "".ifBlank {
                                stringResource(R.string.unknown) },
                            desc = timeAgo(participant.createdAt),
                            data = participant.user?.profileUrl ?: ""
                        )
                    }
                }
            }
        }

        Spacer(modifier.size(ButtonDefaults.IconSize))
        CardFooter(
            modifier = modifier.padding(
                horizontal = 24.dp),
            text = "${model.questions.size} " +
                    if (model.questions.size > 1) "Question\'s" else "Question",
            icon = Icons.Default.QuestionAnswer
        )

        if (isOwn && model.questions.isNotEmpty()){
            Column(
                modifier = modifier.fillMaxWidth()) {

                repeat(model.questions.size) {
                    Box(
                        modifier = modifier.clickable {
                            onQuestionPressed(model.questions[it].id)
                        }
                    ){
                        Divider(
                            thickness = (0.5).dp)

                        Text(
                            modifier = modifier.padding(
                                vertical = 12.dp,
                                horizontal = 24.dp),
                            text = /*"${it.plus(1)}. " + */ model.questions[it].question,
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.secondaryVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}