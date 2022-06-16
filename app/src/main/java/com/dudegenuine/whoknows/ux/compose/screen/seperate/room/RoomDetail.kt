package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.component.misc.CardFooter
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 27 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun RoomDetail(
    modifier: Modifier = Modifier, viewModel: RoomViewModel) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()
    val toggle: () -> Unit = {
        coroutineScope.launch {
            if(scaffoldState.isRevealed) scaffoldState.conceal()
            else scaffoldState.reveal() }}

    viewModel.state.room?.let { model ->
        BackdropScaffold(
            modifier = modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            appBar = {
                GeneralTopBar(
                    light = false,
                    title = "${model.minute} minute\'s duration",
                    leads = Icons.Default.ArrowBack,
                    tails = Icons.Default.Menu,
                    onLeadsPressed = viewModel::onBackRoomDetailPressed,
                    onTailPressed = { toggle() }
                )
            },
            backLayerContent = {
                BackLayer(
                    model = model,
                    viewModel = viewModel,
                    toggle = toggle
                )
            },
            frontLayerContent = {
                FrontLayer(modifier,
                    model = model,
                    viewModel = viewModel
                )
            }
        )
    }

    if (viewModel.state.loading) LoadingScreen(modifier)
    if (viewModel.state.error.isNotBlank()) ErrorScreen(modifier, message = viewModel.state.error)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BackLayer(
    model: Room.Complete,
    viewModel: RoomViewModel,
    modifier: Modifier = Modifier,
    toggle: () -> Unit) {
    var checkedPrivation by remember { mutableStateOf(model.private) }
    val enabled = !model.expired

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {
        ButtonBackLayer(
            label = if (model.isOwner) stringResource(R.string.add_new_question)
                else stringResource(R.string.join_the_room),
            enabled = enabled) {

            if (model.isOwner) viewModel.onNewRoomQuizPressed(model)
            else viewModel.onJoinButtonRoomDetailPressed(model)
        }

        if (model.isOwner) {
            Row(
                modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        ButtonDefaults.MinWidth, ButtonDefaults.MinHeight
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.private_the_class),
                    color = if (enabled) MaterialTheme.colors.onPrimary
                        else MaterialTheme.colors.onPrimary.copy(0.5f),
                    style = MaterialTheme.typography.button
                )
                Switch(
                    checked = checkedPrivation,
                    onCheckedChange = { checkedPrivation = !checkedPrivation },
                    enabled = enabled
                )
            }
            ButtonBackLayer(stringResource(R.string.invite_w_a_link), enabled,
                onLongPressed = { viewModel.onSetCopyRoomPressed(model) }){
                viewModel.onShareRoomPressed(model)
            }
            ButtonBackLayer(stringResource(R.string.delete_permanent), enabled){
                viewModel.onDeleteRoomPressed(model)
            }
            ButtonBackLayer(stringResource(R.string.close_the_room), enabled){
                viewModel.onCloseRoomPressed(model, toggle)
            }
        }
        Spacer(modifier.size(12.dp))
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun FrontLayer(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    model: Room.Complete,
    viewModel: RoomViewModel) {

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
                text = if (model.expired) "Unavailable" else "Available",
                icon = if (model.expired) Icons.Filled.Lock else Icons.Filled.LockOpen,
            )

            CardFooter(
                text = (model.user?.fullName ?: "").ifBlank {
                    model.user?.username ?: stringResource(R.string.unknown) },
                icon = Icons.Filled.VerifiedUser,
            )

            CardFooter(
                text = "${model.impressionSize} ${if(model.impressionSize > 1) "like\'s" else "like"}",
                icon = if(model.impressed) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                color = if(model.impressed) MaterialTheme.colors.error else null
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

                items(model.participants){ participant ->
                    ProfileCard(modifier,
                        name = (participant.user?.fullName ?: "").ifBlank { stringResource(R.string.unknown) },
                        desc = timeAgo(participant.createdAt),
                        data = participant.user?.profileUrl ?: "",
                        onPressed = {
                            if (model.isOwner || participant.isCurrentUser) viewModel.onResultPressed(participant.roomId, participant.userId)
                            else  viewModel.onParticipantItemPressed(participant.userId)
                        },
                        onLongPressed = {
                            if (model.isOwner) viewModel.onParticipantLongPressed(!model.expired, participant)
                        }
                    )
                }
            }
        }

        Spacer(modifier.size(ButtonDefaults.IconSize))
        CardFooter(modifier.padding(horizontal = 24.dp),
            text = "${model.questions.size} " +
                    if (model.questions.size > 1) "Question\'s" else "Question",
            icon = Icons.Default.QuestionAnswer
        )

        if (model.isOwner and model.questions.isNotEmpty()){
            Column(modifier.fillMaxWidth()) {
                model.questions.forEach { quiz ->

                    Box(modifier.combinedClickable(
                        onLongClick = { viewModel.onQuestionLongPressed(model, quiz) },
                        onClick = { viewModel.onQuestionItemPressed(quiz.id) })) {
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

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
private fun ButtonBackLayer(
    label: String, enabled: Boolean, modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    onLongPressed: (() -> Unit)? = null, onPressed: (() -> Unit)? = null) {
    val contentColor by colors.contentColor(enabled)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(enabled, onLongClick = { onLongPressed?.invoke() }) { },
        onClick = { onPressed?.invoke() },
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        color = ButtonDefaults.buttonColors().backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = null,
        elevation = 0.dp,
        interactionSource = interactionSource) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(value = MaterialTheme.typography.button) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){

                    Text(label,
                        color = if (enabled) MaterialTheme.colors.onPrimary
                            else MaterialTheme.colors.onPrimary.copy(0.5f),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }

    /*Box(modifier.combinedClickable(
        enabled = enabled,
        onLongClick = { onLongPressed?.invoke() },
        onClick = { onPressed?.invoke() })) {

        Box(
            modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp),
            contentAlignment = Alignment.Center) {

            Text(label,
                color = if (enabled) MaterialTheme.colors.onPrimary
                else MaterialTheme.colors.onPrimary.copy(0.5f),
                style = MaterialTheme.typography.button
            )
        }
    }*/
}