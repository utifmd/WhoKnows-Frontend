package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.CardFooter
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 27 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun RoomDetail(
    modifier: Modifier = Modifier,
    isOwn: Boolean = false,
    eventDetail: IRoomEventDetail,
    onBackPressed: () -> Unit,
    viewModel: RoomViewModel = hiltViewModel()) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state
    val toggle: () -> Unit = {
        coroutineScope.launch {
            if(scaffoldState.isRevealed) scaffoldState.conceal()
            else scaffoldState.reveal()
        }
    }

    state.room?.let { model ->
        BackdropScaffold(
            modifier = modifier.fillMaxSize(),
            scaffoldState = scaffoldState,

            appBar = {
                GeneralTopBar(
                    light = false,
                    title = "${model.minute} minute\'s duration",
                    leads = Icons.Default.ArrowBack,
                    tails = Icons.Default.Menu,
                    onLeadsPressed = onBackPressed,
                    onTailPressed = { toggle() }
                )
            },

            backLayerContent = {
                BackLayer(
                    model = model,
                    isOwn = isOwn,
                    toggle = toggle,
                    eventDetail = eventDetail
                )
            },

            frontLayerContent = {
                FrontLayer(modifier,
                    model = model, isOwn = isOwn,
                    currentUserId = viewModel.currentUserId,
                    onProfileSelected = eventDetail::onParticipantItemPressed,
                    onProfileLongPressed = eventDetail::onParticipantLongPressed,
                    onQuestionPressed = eventDetail::onQuestionItemPressed,
                    onQuestionLongPressed = eventDetail::onQuestionLongPressed,
                    onResultSelected = eventDetail::onResultPressed
                )
            }
        )
    }

    if (state.loading) LoadingScreen(modifier)
    if (state.error.isNotBlank()) ErrorScreen(modifier, message = state.error)
}

@Composable
private fun BackLayer(
    modifier: Modifier = Modifier,
    model: Room.Complete,
    toggle: () -> Unit,
    isOwn: Boolean,
    eventDetail: IRoomEventDetail) {
    val enabled = !model.expired

    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {

        TextButton(
            enabled = enabled,
            modifier = modifier.fillMaxWidth(),
            onClick = {
                if (isOwn) eventDetail.onNewRoomQuizPressed(model.id, model.userId)
                else eventDetail.onJoinRoomDirectlyPressed(model) }) {

            Text(
                color = if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(0.5f),

                text = if (isOwn) stringResource(R.string.add_new_question)
                    else stringResource(R.string.join_the_room)
            )
        }

        if (isOwn) {
            TextButton(
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = { eventDetail.onShareRoomPressed(model) }) {

                Text(
                    color = if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(0.5f),
                    text = stringResource(R.string.invite_w_a_link)
                )
            }

            TextButton(
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = { eventDetail.onDeleteRoomPressed(model) } ) {

                Text(stringResource(R.string.delete_permanent),
                    color =  if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(0.5f),
                )
            }

            TextButton(
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = { eventDetail.onCloseRoomPressed(model, toggle) }) {

                Text(
                    color = if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(0.5f),
                    text = stringResource(R.string.close_the_room)
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun FrontLayer(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    model: Room.Complete, currentUserId: String,
    isOwn: Boolean,
    onQuestionPressed: (String) -> Unit,
    onQuestionLongPressed: (enabled: Boolean, quiz: Quiz.Complete, roomId: String) -> Unit,
    onProfileSelected: (String) -> Unit,
    onProfileLongPressed: (Boolean, Participant) -> Unit,
    onResultSelected: (String, String) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        contentModifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Spacer(modifier.height(36.dp))

        Column(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text(model.title,
                style = MaterialTheme.typography.h4)

            CardFooter(
                text = model.createdAt.toHttpDateString(),
                icon = Icons.Filled.CalendarToday,
            )

            CardFooter(
                text = "${if (model.expired) "Closed" else "Opened"} by ${
                    model.user?.fullName ?:
                    model.user?.username?.padStart(1, '@') ?:
                    stringResource(R.string.unknown)
                }",
                icon = if (model.expired) Icons.Filled.Lock else Icons.Filled.LockOpen,
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(model.description, style = MaterialTheme.typography.body2)
            }
        }
        
        Spacer(modifier.size(ButtonDefaults.IconSize))
        CardFooter(modifier.padding(horizontal = 24.dp),
            text = "${model.participants.size} " +
                    if (model.participants.size > 1)
                        "Participant\'s" else "Participant",
            icon = Icons.Default.People,
        )

        if (model.participants.isNotEmpty()) {
            LazyRow(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)) {

                model.participants
                    .sortedByDescending { it.userId == currentUserId }
                    .forEach { participant ->

                    item {
                        ProfileCard(modifier,
                            name = participant.user?.fullName ?: "".ifBlank {
                                stringResource(R.string.unknown) },
                            desc = timeAgo(participant.createdAt),
                            data = participant.user?.profileUrl ?: "",
                            onPressed = {
                                if (isOwn || participant.userId == currentUserId)
                                    onResultSelected(participant.roomId, participant.userId)
                                else  onProfileSelected(participant.userId)
                            },
                            onLongPressed = {
                                if (isOwn) onProfileLongPressed(!model.expired, participant)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier.size(ButtonDefaults.IconSize))
        CardFooter(modifier.padding(horizontal = 24.dp),
            text = "${model.questions.size} " +
                    if (model.questions.size > 1) "Question\'s" else "Question",
            icon = Icons.Default.QuestionAnswer
        )

        if (isOwn and model.questions.isNotEmpty()){
            Column(modifier.fillMaxWidth()) {
                model.questions.forEach { quiz ->

                    Box(modifier.combinedClickable(
                        onLongClick = { onQuestionLongPressed(!model.expired and model.participants.isEmpty(), quiz, model.id) },
                        onClick = { onQuestionPressed(quiz.id) })) {
                        Divider(thickness = (0.5).dp)

                        Text(quiz.question,
                            modifier = modifier.padding(
                                vertical = 12.dp,
                                horizontal = 24.dp),
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