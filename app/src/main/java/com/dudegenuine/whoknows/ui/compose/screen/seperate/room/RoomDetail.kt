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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
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
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 27 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RoomDetail(
    modifier: Modifier = Modifier,
    isOwn: Boolean = false,
    viewModel: RoomViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    eventRouter: IRoomEventDetail, onBackPressed: () -> Unit) {
    val state = viewModel.state
    val context =  LocalContext.current

    var modalDialog by remember { mutableStateOf(DialogState()) }
    val onModalDismissed: () -> Unit = { modalDialog = DialogState() }
    val toggle: () -> Unit = {
        coroutineScope.launch {
            if(scaffoldState.isRevealed) scaffoldState.conceal()
            else scaffoldState.reveal()
        }
    }
    val eventCompose = object: IRoomEventDetail {
        override fun onShareRoomPressed(roomId: String) { viewModel.onSharePressed(roomId) }
        override fun onParticipantLongPressed(expired: Boolean, participant: Participant) {
            modalDialog = DialogState(context.getString(R.string.delete_participant), true, if(!expired) {
                { viewModel.deleteParticipation(participant) { eventRouter.onRoomRetailPressed(participant.roomId) } }} else null)
        }
        override fun onQuestionLongPressed(enabled: Boolean, quiz: Quiz.Complete, roomId: String) {
            modalDialog = DialogState(context.getString(R.string.delete_question), true, if(enabled) {
                { viewModel.deleteQuestion(quiz) { eventRouter.onRoomRetailPressed(roomId) }}} else null)
        }
        override fun onCloseRoomPressed(room: Room.Complete) {
            modalDialog = DialogState(context.getString(R.string.close_the_class), true) {
                viewModel.expireRoom(room) { toggle() } }
        }
        override fun onJoinRoomDirectlyPressed(room: Room.Complete) {
            modalDialog = DialogState(context.getString(R.string.participate_the_class), true, if (viewModel.currentUserId.isNotBlank()) {{
                eventRouter.onBoardingRoomPressed(room.id); toggle() }} else null)
        }
        override fun onDeleteRoomPressed(roomId: String) {
            modalDialog = DialogState(context.getString(R.string.delete_class), true) {
                viewModel.deleteRoom(roomId) { eventRouter.onDeleteRoomSucceed(it) }
            }
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
                    evenCompose = eventCompose,
                    evenRouter = eventRouter
                )
            },

            frontLayerContent = {
                FrontLayer(modifier,
                    model = model, isOwn = isOwn,
                    currentUserId = viewModel.currentUserId,
                    onProfileSelected = eventRouter::onParticipantItemPressed,
                    onProfileLongPressed = { eventCompose.onParticipantLongPressed(model.expired, it) },
                    onQuestionPressed = eventRouter::onQuestionItemPressed,
                    onQuestionLongPressed = eventCompose::onQuestionLongPressed,
                    onResultSelected = eventRouter::onResultPressed
                )
            }
        )
    }

    if (state.loading) LoadingScreen(modifier)
    if (state.error.isNotBlank()) ErrorScreen(modifier, message = state.error)
    if (modalDialog.opened) with (modalDialog) {
        AlertDialog(
            modifier = modifier.padding(horizontal = 24.dp),
            onDismissRequest = onModalDismissed,
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton({ event?.invoke(); onModalDismissed() },
                    enabled = event != null) {

                    Text((button ?: "submit").replaceFirstChar { it.uppercase() })
                }
            }
        )
    }
}

@Composable
private fun BackLayer(
    modifier: Modifier = Modifier,
    model: Room.Complete,
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
                enabled = enabled and (model.questions.size >= 3),
                modifier = modifier.fillMaxWidth(),
                onClick = { evenCompose.onShareRoomPressed(model.id) }) {

                Text(
                    color = if (enabled) MaterialTheme.colors.onPrimary else Color.LightGray,
                    text = "Invite with a link"
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
                enabled = enabled,
                modifier = modifier.fillMaxWidth(),
                onClick = { evenCompose.onDeleteRoomPressed(model.id) }) {

                Text(
                    color =  if (enabled) MaterialTheme.colors.onPrimary else Color.LightGray,
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
    model: Room.Complete, currentUserId: String,
    isOwn: Boolean,
    onQuestionPressed: (String) -> Unit,
    onQuestionLongPressed: (enabled: Boolean, quiz: Quiz.Complete, roomId: String) -> Unit,
    onProfileSelected: (String) -> Unit,
    onProfileLongPressed: (Participant) -> Unit,
    onResultSelected: (String, String) -> Unit) {
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

        if (model.participants.isNotEmpty()) {
            LazyRow(modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)) {

                model.participants.forEach { participant ->
                    item {
                        ProfileCard(
                            modifier = modifier.combinedClickable(
                                onLongClick = { if (isOwn) onProfileLongPressed(participant) },
                                onClick = {
                                    if (isOwn || participant.userId == currentUserId)
                                        onResultSelected(participant.roomId, participant.userId)
                                    else  onProfileSelected(participant.userId)
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