package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
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
    modifier: Modifier = Modifier, viewModel: RoomViewModel,
    setIsRefresh: ((Boolean) -> Unit)? = null, onBackPressed: () -> Unit) {

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()

    fun toggle() = coroutineScope.launch {
        if(scaffoldState.isRevealed) scaffoldState.conceal()
        else scaffoldState.reveal()
    }
    BackHandler(onBack = onBackPressed)
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
                    onLeadsPressed = onBackPressed, //if(isRefresh) viewModel::onBackRoomDetailPressed else viewModel::onBackPressed,
                    onTailPressed = ::toggle
                )
            },
            backLayerContent = {
                BackLayer(
                    model = model,
                    setIsRefresh = setIsRefresh ?: {},
                    viewModel = viewModel,
                    toggle = ::toggle
                )
            },
            frontLayerContent = {
                FrontLayer(modifier,
                    model = model,
                    setIsRefresh = setIsRefresh ?: {},
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
    model: Room.Complete, viewModel: RoomViewModel,
    setIsRefresh: (Boolean) -> Unit, toggle: () -> Unit) {
    val enabled = !model.expired
    val (exclusive, setExclusive) = remember { mutableStateOf(model.private) }
    val (messaging, setMessaging) = remember { mutableStateOf(model.token.isNotBlank()) }
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (model.isOwner) {
            ButtonBackLayer(stringResource(R.string.delete_permanent), enabled) {
                viewModel.onDeleteRoomPressed(model)
            }
            ButtonBackLayer(stringResource(R.string.close_the_room), enabled) {
                viewModel.onCloseRoomPressed(model, toggle)
            }
            ButtonBackLayer(stringResource(R.string.invite_w_a_link), enabled,
                onLongPressed = { viewModel.onSetCopyRoomPressed(model) }) {
                viewModel.onShareRoomPressed(model)
            }
            ButtonBackLayer(
                label = stringResource(R.string.add_new_question),
                enabled = enabled) {
                viewModel.onNewRoomQuizPressed(model)
            }
            Divider(thickness = (0.5).dp)
            SwitchBackLayer(
                label = stringResource(R.string.private_the_class),
                checked = exclusive,
                enabled = enabled && !viewModel.state.loading) { selected ->
                viewModel.onExclusiveClassChange(model, selected) { setExclusive(selected) }
            }
            Divider(thickness = (0.5).dp)
            ToggleBackLayer(
                icon = if (messaging)
                    Icons.Default.NotificationsActive else
                        Icons.Default.NotificationsOff,
                label = stringResource(R.string.notification_class),
                enabled = enabled,
                checked = messaging) { selected ->
                viewModel.onMessagingChange(selected, model)
                setMessaging(!messaging)
            }
        } else if (model.isParticipant) ButtonBackLayer(
            label = stringResource(R.string.leave_the_class),
            enabled = enabled) {
            viewModel.onLeaveRoomPressed(enabled, model, setIsRefresh)
        } else ButtonBackLayer(
            label = stringResource(R.string.join_the_room),
            enabled = enabled) {
            viewModel.onJoinButtonRoomDetailPressed(model)
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
private fun FrontLayer(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier, viewModel: RoomViewModel,
    model: Room.Complete, setIsRefresh: (Boolean) -> Unit) {
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
                icon = Icons.Filled.CalendarViewMonth,
            )

            CardFooter(
                text = if (model.expired) "Unavailable" else "Available",
                icon = if (model.expired) Icons.Filled.Lock else Icons.Filled.LockOpen,
            )

            CardFooter(
                text = (model.user?.fullName ?: EMPTY_STRING).ifBlank {
                    model.user?.username ?: stringResource(R.string.unknown) },
                icon = Icons.Outlined.VerifiedUser,
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
        Chip({}, modifier = modifier.padding(horizontal = 24.dp)) {
            Icon(Icons.Default.People, contentDescription = null)
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
            Text("${model.participants.size} " +
                    if (model.participants.size > 1) "Participant\'s" else "Participant",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.caption
            )
        }
        if (model.participants.isNotEmpty()) {
            LazyRow(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)) {

                items(model.participants){ participant ->
                    ProfileCard(modifier.fillMaxSize(),
                        name = participant.user?.fullName ?: stringResource(R.string.unknown),
                        desc = timeAgo(participant.createdAt).plus(" ~ ${if(participant.expired) "done" else "in progress"}"),
                        data = participant.user?.profileUrl ?: EMPTY_STRING,
                        colorBorder = if (!participant.expired)
                            MaterialTheme.colors.onError else null,
                        onPressed = {
                            Log.d("TAG", "model.isOwner: ${model.isOwner}")
                            Log.d("TAG", "model.isParticipated: ${model.isParticipated}")
                            Log.d("TAG", "model.isParticipant: ${model.isParticipant}")
                            Log.d("TAG", "participant.isCurrentUser: ${participant.isCurrentUser}")

                            if (model.isOwner || model.isParticipant)
                                viewModel.onResultPressed(participant.roomId, participant.userId)
                            else viewModel.onParticipantItemPressed(participant.userId)
                        },
                        onLongPressed = {
                            if (model.isOwner) viewModel.onParticipantLongPressed(!model.expired, participant, setIsRefresh)
                        }
                    )
                }
            }
        }
        Spacer(modifier.size(ButtonDefaults.IconSize))
        Chip({}, modifier = modifier.padding(horizontal = 24.dp)){
            Icon(Icons.Default.Task, contentDescription = null)
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
            Text("${model.questions.size} " +
                    if (model.questions.size > 1) "Question\'s" else "Question",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.caption
            )
        }
        if (model.isOwner and model.questions.isNotEmpty()){
            Column(modifier.fillMaxWidth()) {
                model.questions.forEach { quiz ->

                    Box(modifier.combinedClickable(
                        onLongClick = { viewModel.onQuestionLongPressed(model, quiz, setIsRefresh) },
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
        Spacer(modifier.size(ButtonDefaults.IconSize))
    }
}

@Composable
private fun SwitchBackLayer(
    modifier: Modifier = Modifier, label: String, enabled: Boolean,
    checked: Boolean, setChecked: (Boolean) -> Unit) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = ButtonDefaults.IconSize)
            .defaultMinSize(ButtonDefaults.MinWidth, ButtonDefaults.MinHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(label,
            color = if (enabled)
                MaterialTheme.colors.onPrimary else
                MaterialTheme.colors.onPrimary.copy(0.5f),
            style = MaterialTheme.typography.caption)
        Switch(
            checked = checked,
            onCheckedChange = setChecked,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.onPrimary,
            )
        )
    }
}

@Composable
private fun ToggleBackLayer(
    modifier: Modifier = Modifier, icon: ImageVector, label: String, enabled: Boolean,
    checked: Boolean, setChecked: (Boolean) -> Unit) {
    //val (checked, onCheckedChange) = remember { mutableStateOf(false) }
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = ButtonDefaults.IconSize)
            .defaultMinSize(ButtonDefaults.MinWidth, ButtonDefaults.MinHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(label,
            color = if (enabled)
                MaterialTheme.colors.onPrimary else
                MaterialTheme.colors.onPrimary.copy(0.5f),
            style = MaterialTheme.typography.caption)
        IconToggleButton(
            checked = checked,
            onCheckedChange = setChecked) {
            Icon(icon, contentDescription = null)
        }
    } //Box(modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){ }
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
}