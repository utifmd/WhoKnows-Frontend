package com.dudegenuine.whoknows.ux.vm.room

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.workDataOf
import com.dudegenuine.model.*
import com.dudegenuine.repository.contract.dependency.local.IResourceDependency
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.KEY_ROOM_TOKEN_RESULT
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.TAG_ROOM_TOKEN
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ux.compose.model.Dialog
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.state.NotificationState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DESC_TOO_LONG
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ux.compose.state.RoomState
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RoomViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    private val caseNotification: INotificationUseCaseModule,
    private val caseRoom: IRoomUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val caseParticipant: IParticipantUseCaseModule,
    private val caseQuiz: IQuizUseCaseModule,
    private val caseResult: IResultUseCaseModule,
    private val savedStateHandle: SavedStateHandle): IRoomViewModel() {
    @Inject lateinit var resource: IResourceDependency
    private val TAG: String = javaClass.simpleName
    private val worker = caseRoom.workManager.instance()
    private val prefs = caseRoom.preferences

    private val userId get() = prefs.userId
    val receiver get() = caseRoom.receiver

    private val _roomState = mutableStateOf(RoomState())
    val roomState get() = _roomState.value

    init {
        //onParticipationRouted()
        //onParticipationStored()
        onDetailRouted()
        //onParticipated()
    }
    internal fun subscribeTokenWorker(){
        val tokenWorkRequest = caseRoom.workRequest.onTime() /*.periodicTime(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)*/
            .addTag(TAG_ROOM_TOKEN)
            .setInputData(workDataOf(KEY_ROOM_TOKEN_RESULT to "Parameter value from $TAG"))//.setConstraints(networkConnectedConstrains)
            .build()

        worker.enqueue(tokenWorkRequest)

        worker.getWorkInfoByIdLiveData(tokenWorkRequest.id).asFlow()
            .onEach(::onWorkResult)
            .onCompletion { if(it == null) Log.d(TAG, "subscribeAlarmWorker: complete") }
            .launchIn(viewModelScope)
    }
    internal fun cancelWork() = worker.cancelAllWorkByTag(TAG_ROOM_TOKEN)
    private fun onDetailRouted() =
        savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)?.let(::getRoom)

    fun onExclusiveClassChange(
        room: Room.Complete, selected: Boolean, onSelected: () -> Unit){
        if (userId.isBlank() || room.id.isBlank()) return
        val dialog = Dialog(
            about = if (selected) "Eksklusif" else "Inklusif",
            disclaimer = resource.string(R.string.room_exclusive_disclaimer)){
            onSelected()
            patchRoom(room.id, room.copy(private = selected))
        }
        onScreenStateChange(ScreenState.AlertDialog(dialog))
    }
    fun onLeaveRoomPressed(room: Room.Complete){

    }
    private fun onSharePressed(roomId: String) {
        val data = "${BuildConfig.BASE_CLIENT_URL}/who-knows/room/$roomId"

        caseRoom.share.launch(data)
    }
    private fun onCopyRoomIdPressed(roomId: String) =
        caseRoom.clipboard.applyPlainText("ROOM ID", roomId)

    fun onCreatePressed() {
        val model = roomState.room.copy(userId = userId)

        if (!roomState.isPostValid || userId.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        if (roomState.desc.text.length > 225){
            onStateChange(ResourceState(error = DESC_TOO_LONG))
            return
        }
        caseRoom.postRoom(model)
            .onCompletion{ if (it == null) onNavigateBackThenRefresh() }
            .launchIn(viewModelScope)
    }
    private fun onDeleteRoom(
        room: Room.Complete) {
        if (room.id.isBlank() || room.token.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseRoom.deleteRoom(room)
            .onEach(::onResource)
            .onCompletion{ if (it == null) onNavigateBackThenRefresh() }
            .launchIn(viewModelScope)
    }

    override fun onNavigateBackThenRefresh() =
        onScreenStateChange(ScreenState.Navigate.Back(refresh = true))

    private fun onDeleteQuestionPressed(quiz: Quiz.Complete, setIsRefresh: (Boolean) -> Unit) {
        val flowFileIds = quiz.images.map { it.substringAfterLast("/") }
        Log.d(TAG, "onDeleteQuestionPressed: triggered")

        flowOf(
            caseQuiz.deleteQuiz(quiz.id),
            flowFileIds.asFlow()
                .flatMapConcat{ caseFile.deleteFile(it) })
            .flattenMerge()
            .onCompletion{
                if (it == null) getRoom(quiz.roomId)
                setIsRefresh(true)
            }
            .launchIn(viewModelScope)
    }

    private fun onDeleteParticipantPressed(
        participant: Participant, setIsRefresh: (Boolean) -> Unit){

        caseParticipant.deleteParticipation(participant)
            .onEach(::onResourceStateless)
            .onCompletion{
                if (it == null) getRoom(participant.roomId)
                setIsRefresh(true)
            }
            .launchIn(viewModelScope)
    }

    private fun flowUnregisterMessaging(room: Room.Complete, participant: Participant): Flow<Resource<out Any>> {
        val event = "${participant.user?.username} $JUST_KICKED_OUT ${room.title} class"
        val notifier = NotificationState().copy(
            userId = userId,
            roomId = participant.roomId,
            event = event,
            recipientId = participant.userId
        )
        val pusher = Messaging.Pusher(room.title, event,
            largeIcon = participant.user?.profileUrl ?: "",
            args = mapOf("roomId" to room.id, "userId" to participant.user?.userId).toString())

        return flowOf(
            caseNotification.postNotification(notifier),
            caseMessaging.getMessaging(participant.roomId)
                .flatMapConcat { res -> onResourceFlow(res) { key ->
                    caseMessaging.pushMessaging(pusher.copy(to = key))
                }})
            .flattenMerge()
    }

    override fun postRoom(room: Room.Complete) {
        if (room.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        room.apply { createdAt = Date() }

        caseRoom.postRoom(room)
            .onEach(::onResource).launchIn(viewModelScope)
    }
    override fun getRoom(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseRoom.getRoom(id)
            .onEach(::onResource)
            //.onEach{ res -> onResourceSucceed(res, ::onDetailingRoom) }
            .launchIn(viewModelScope)
    }
    private fun getRoom(id: String, onSucceed: (Room.Complete) -> Unit) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseRoom.getRoom(id)
            .onEach{ onResourceSucceed(it, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room.Complete) {
        if (id.isBlank() || current.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
        }
        current.apply { updatedAt = Date() }

        caseRoom.patchRoom(id, current)
            .onEach(::onResourceStateless)
            .launchIn(viewModelScope)
    }

    fun expireRoom(current: Room.Complete, finished: () -> Unit) {
        val model = current.copy(expired = true, updatedAt = Date())
        if (model.id.isBlank() || model.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
        }

        caseRoom.patchRoom(model.id, model)
            .onEach{ res -> onResourceSucceed(res) {
                onStateChange(ResourceState(room = it))

                finished() //onSucceed(it)
            }}
            .launchIn(viewModelScope)
    }

    override fun deleteRoom(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseRoom.deleteRoom(id)
            .onEach{ onResourceSucceed(it){ onNavigateBackThenRefresh() }}
            .launchIn(viewModelScope)
    }

    override val roomsCensored: Flow<PagingData<Room.Censored>> =
        caseRoom.getRooms(DEFAULT_BATCH_ROOM)
            .cachedIn(viewModelScope)

    override val roomsComplete: Flow<PagingData<Room.Complete>> =
        caseRoom.getRooms(prefs.userId, DEFAULT_BATCH_ROOM)
            .cachedIn(viewModelScope)

    override fun getRooms(page: Int, size: Int) {
        if (size == 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
        }
        caseRoom.getRooms(page, size)
            .onEach(::onResource).launchIn(viewModelScope)
    }
    override fun createMessaging(
        messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.operation.isBlank() or model.keyName.isBlank() or model.tokens.isEmpty())
            onStateChange(ResourceState(error = DONT_EMPTY))

        caseMessaging.createMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }
    override fun addMessaging(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.keyName.isBlank() or model.key.isBlank() or model.tokens.isEmpty()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseMessaging.addMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) } //(::onResource)
            .launchIn(viewModelScope)
    }
    private fun getCurrentUser(onSucceed: (User.Complete) -> Unit) {
        caseUser.getUser(userId)
            .onEach{ res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }
    fun onRoomFinderButtonGoPressed(roomId: String) =
        getRoom(roomId){ onNavigateTo(Screen.Home.Discover.RoomDetail.routeWithArgs(roomId)) }

    //override fun onImpressed() { TODO("Not yet implemented") }
    override fun onNewClassPressed() =
        onNavigateTo(Screen.Home.Summary.RoomCreator.route)
    override fun onNotificationPressed() =
        onNavigateTo(Screen.Home.Summary.Notification.route)
    override fun onButtonJoinRoomWithACodePressed() =
        onNavigateTo(Screen.Home.Summary.RoomFinder.route)
    override fun onRoomHomeScreenDetailSelected(id: String) =
        onNavigateTo(Screen.Home.Summary.RoomDetail.routeWithArgs(id))
    override fun onNewRoomQuizPressed(room: Room.Complete) { if (room.participants.isEmpty())
        onNavigateTo(Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(room.id, room.userId)) else
            onShowSnackBar(resource.string(R.string.already_participantion)) }
    override fun onParticipationDecided(roomId: String) {
        val screen = Screen.Home.Summary.Participation
        val option = NavOptions.Builder().setPopUpTo(Screen.Home.route, true).build()
        onScreenStateChange(ScreenState.Navigate.To(screen.routeWithArgs(roomId), option))
    }
    override fun onParticipantItemPressed(userId: String) =
        onNavigateTo(Screen.Home.Summary.RoomDetail.ProfileDetail.routeWithArgs(userId))
    override fun onResultPressed(roomId: String, userId: String) =
        onNavigateTo(Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(roomId, userId))
    override fun onQuestionItemPressed(quizId: String) =
        onNavigateTo(Screen.Home.Summary.RoomDetail.QuizDetail.routeWithArgs(quizId))
    override fun onBackPressed() = onNavigateBack()
    override fun onBackRoomDetailPressed() = onNavigateBackThenRefresh() /*{
        val option = NavOptions.Builder().setPopUpTo(Screen.Home.route, true).build()
        onScreenStateChange(ScreenState.Navigate.To(Screen.Home.route, option))
    }*/
    /*
    private val _isRoomAlarmUp = mutableStateOf(prefs.roomAlarm)
    val isRoomAlarmUp get() = _isRoomAlarmUp.value
    fun onIsAlarmUpChange(minute: Int, selected: Boolean){
        if (selected) caseRoom.alarmManager.setupMinute(minute)
        else caseRoom.alarmManager.cancel()

        _isRoomAlarmUp.value = selected
        prefs.roomAlarm = isRoomAlarmUp
    }
    private val _isRoomNotification = mutableStateOf(prefs.roomAlarm)
    val isRoomNotification get() = _isRoomNotification.value
    */
    fun onMessagingChange(selected: Boolean, room: Room.Complete) =
        caseRoom.patchRoom(selected, room)
            .onEach(::onResourceStateless)
            .launchIn(viewModelScope)

    override fun onShareRoomPressed(room: Room.Complete) { if(room.questions.size >= 3)
        onSharePressed(room.id) else
            onShowSnackBar(resource.string(R.string.allowed_after_add_3_quest))
    }
    override fun onSetCopyRoomPressed(room: Room.Complete) { if(room.questions.size >= 3)
        onCopyRoomIdPressed(room.id) else
            onShowSnackBar(resource.string(R.string.allowed_after_add_3_quest))
    }
    override fun onJoinButtonRoomDetailPressed(room: Room.Complete) {
        val disclaimer = if(room.isParticipated)
            resource.string(R.string.already_re_joined_the_class) else if (!room.isJoinAccepted)
                    resource.string(R.string.already_joined_the_class) else null

        fun onSubmit() = onParticipationDecided(room.id)
        onShowDialog(Dialog(resource.string(R.string.participate_the_class), disclaimer,
            onSubmitted = if(prefs.participationRoomId.isNotBlank()) null
            else if(room.isJoinAccepted || room.isParticipated) ::onSubmit else null))
    }
    override fun onCloseRoomPressed(room: Room.Complete, onComplete: () -> Unit) {
        val disclaimer = resource.string(R.string.no_longer_participation)
        onShowDialog(Dialog(resource.string(R.string.close_the_class), disclaimer) { if(room.questions.size >= 3)
            expireRoom(room, onComplete) else
                onShowSnackBar(resource.string(R.string.allowed_after_add_3_quest)) })
    }
    override fun onDeleteRoomPressed(
        room: Room.Complete) {
        val disclaimer = when {
            room.participants.isNotEmpty() -> resource.string(R.string.already_participantion)
            room.questions.any { it.images.isNotEmpty() } -> resource.string(R.string.there_is_no_quest)
            else -> null
        }
        val accepted = room.participants.isEmpty() && room.questions.all { it.images.isEmpty() }
        onShowDialog(
            Dialog(resource.string(R.string.delete_class), disclaimer,
            onSubmitted = if (accepted) {
                { onDeleteRoom(room) }} else null)
        )
    }
    override fun onParticipantLongPressed(
        enabled: Boolean, participant: Participant, setIsRefresh: (Boolean) -> Unit) = onShowDialog(
            Dialog(resource.string(R.string.delete_participant),
            if (!enabled) resource.string(R.string.allowed_when_class_opened) else null,
            onSubmitted = if(enabled) {
                { onDeleteParticipantPressed(participant, setIsRefresh) }} else null)
        )
    override fun onQuestionLongPressed(
        room: Room.Complete,
        quiz: Quiz.Complete,
        setIsRefresh: (Boolean) -> Unit) {
        val accepted = !room.expired && room.participants.isEmpty()
        val disclaimer: String? = when{
            room.expired -> resource.string(R.string.allowed_when_class_opened)
            room.participants.isNotEmpty() -> resource.string(R.string.already_participantion)
            else -> null}
        onShowDialog(
            Dialog(resource.string(R.string.delete_question), disclaimer,
            onSubmitted = if(accepted) {
                { onDeleteQuestionPressed(quiz, setIsRefresh) }} else null)
        )
    }
}