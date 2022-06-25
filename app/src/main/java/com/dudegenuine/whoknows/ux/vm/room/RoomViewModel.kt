package com.dudegenuine.whoknows.ux.vm.room

import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.launch
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

    /*fun `check that any current user participan is not expired`(){
    }*/

    /*private fun onParticipated() = viewModelScope.launch {
        *//*val route = Screen.Home.Summary.Participation.routeWithArgs("")*//*
        participationState.collectLatest { participation ->
            if (participation !is Participation.OnBoarding) return@collectLatest

            Log.d(TAG, "onParticipated: $participation")
        }
    }*/

    /*internal fun checkAlarm(){
        val nextAlarm = caseRoom.alarmManager.alarmManager.nextAlarmClock
        Log.d(TAG, "checkAlarm: isBroadcast ${nextAlarm.showIntent.creatorPackage}")
    }*/
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
    internal fun cancelWork(){
        worker.cancelAllWorkByTag(TAG_ROOM_TOKEN)
    }
    /*val timerReceiver: (Room.State.BoardingQuiz) -> BroadcastReceiver = { roomState ->
        receiver.timerReceiver { time, finished ->
            formState.onTimerChange(time)
            if (finished) onPreResult(roomState)
        }
    }*/

    /*override fun onTimerReceiver(participation: Participation.OnBoarding): BroadcastReceiver =
        receiver.timerReceiver { time, finished ->
            formState.onTimerChange(time)
            if (finished) onPreResult(participation)
        }*/

    private fun onDetailRouted() =
        savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)?.let(::getRoom)

    private fun onParticipationRouted() {
        val roomId = savedStateHandle.get<String>(KEY_PARTICIPATION_ROOM_ID)

        if (!roomId.isNullOrBlank()) onPreBoarding(roomId)
    }

    private fun onParticipationStored() {
        /*Log.d(TAG, "onParticipationStored: triggered")
        if (currentUserId.isBlank() or prefs.participationId.isBlank()) return
        Log.d(TAG, "onParticipationStored: passed")
        *//*caseRoom.getBoarding()
            .onEach{ onResourceSucceed(it) { participation ->
                Log.d(TAG, "onParticipationStored: ${prefs.participationId}")
                *//**//*if (prefs.runningTime <= 0) onPreResult(participation) //is finished
                else onRoomStateChange(participation)*//**//*
                onParticipationPressed(participation.roomId)
            }}
            .launchIn(viewModelScope)*//*
        onNavigateTo(Screen.Home.Summary.Participation.routeWithArgs(""))*/
    }
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
    fun onNotificationClassChange(room: Room.Complete, off: Boolean){
        // TODO: each participant can personally get or off notification
        /*if (prefs.tokenId.isBlank() || room.id.isBlank()) return
        if (off) caseMessaging.removeMessaging(Messaging.GroupRemover(
            tokens = listOf(prefs.tokenId), keyName = room.id, key = room.token))
            .launchIn(viewModelScope)
        else caseMessaging.createMessaging(Messaging.GroupCreator(
            tokens = listOf(prefs.tokenId), keyName = room.id))
            .launchIn(viewModelScope)*/

        /*if (off) caseMessaging.removeMessaging(Messaging.GroupRemover(
            tokens = listOf(prefs.tokenId), keyName = room.id, key = room.token))
            .launchIn(viewModelScope)
        else caseMessaging.addMessaging(Messaging.GroupAdder(
            tokens = listOf(prefs.tokenId), keyName = room.id, key = room.token))
            .launchIn(viewModelScope)*/
    }
    private fun onPreBoarding(roomId: String) {
        Log.d(TAG, "onPreBoarding: $roomId")
        if (roomId.isBlank()) {
            onScreenStateChange(ScreenState.Toast("unknown participation", Toast.LENGTH_LONG))
            return
        }
        caseRoom.getRoom(roomId)
            .onEach { res -> onResourceSucceed(res, ::onInitBoarding) }
            .launchIn(viewModelScope)
    }

    private fun onInitBoarding(room: Room.Complete) {
        /*Log.d(TAG, "onInitBoarding: triggered")

        val model = formState.participant.copy(
            roomId = room.id, userId = currentUserId, timeLeft = room.minute)

        val asSecond = (room.minute.toFloat() * 60).toDouble()

        caseRoom.timer.start(asSecond)

        onBoarding(room, formState.participant.id)*/

        /*caseParticipant.postParticipant(model).onEach { res ->
            onResource(
                resources = res,
                onSuccess = {
                    caseRoom.timer.start(asSecond)
                    onBoarding(room, it.id)
                },
                onError = { error ->
                    onStateChange(ResourceState(error = ALREADY_JOINED))
                    onShowSnackBar(error)
                }
            )
        }.launchIn(viewModelScope)*/
    }

    /*private fun onBoarding(room: Room.Complete, participantId: String){
        Log.d(TAG, "onBoarding: triggered")
        getCurrentUser { currentUser ->
            val participation = caseParticipant.getParticipation(participantId, room, currentUser)

            //onParticipationStateChange(participation)
        }
    }*/

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
        caseMessaging.createMessaging(Messaging.GroupCreator(
            keyName = model.id, tokens = listOf(prefs.tokenId)))
            .onEach(::onResource)
            .flatMapConcat{ res -> onResourceFlow(res){ notification_key ->
                caseRoom.postRoom(model.copy(token = notification_key))
                    .mapLatest{ Resource.Success(notification_key) }}}
            .onCompletion{ if (it == null) onNavigateBackThenRefresh() }
            .launchIn(viewModelScope)
    }
    private fun onDeleteRoom(
        room: Room.Complete) {
        if (room.id.isBlank() || room.token.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseMessaging.removeMessaging(Messaging.GroupRemover(
            key = room.token, keyName = room.id, tokens = listOf(prefs.tokenId)))
            .onEach(::onResource)
            .flatMapMerge{ caseRoom.deleteRoom(room.id) }
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
        participant: Participant, setIsRefresh: (Boolean) -> Unit) = viewModelScope.launch {
        val room = state.room?.copy() ?: let {
            val message = "invalid current class"
            onStateChange(ResourceState(message = message))
            Log.d(TAG, message)
            return@launch
        }
        flowOf(
            caseParticipant.deleteParticipant(participant.id),
            caseResult.deleteResult(participant.roomId, participant.userId),
            flowUnregisterMessaging(room, participant))
            .flattenMerge()
            .onCompletion{
                if (it == null) getRoom(participant.roomId)
                setIsRefresh(true)
            } //.catch{ it.let(::onResolveAddMessaging) }
            .launchIn(viewModelScope)
    }

    private fun flowUnregisterMessaging(room: Room.Complete, participant: Participant): Flow<Resource<out Any>> {
        val event = "${participant.user?.username} $JUST_KICKED_OUT ${room.title} class"
        val notifier = caseNotification.getNotification().copy(
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

    /*var detailedRoom
        by mutableStateOf<Room.Complete?>(null)
    private set
    fun onDetailRoomChange(room: Room.Complete?){
        detailedRoom = room
    }*/

    override fun getRoom(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseRoom.getRoom(id)
            .onEach{ res -> onResourceSucceed(res, ::onDetailingRoom) }
            .launchIn(viewModelScope)
    }

    private fun onDetailingRoom(room: Room.Complete){
        val isUserOffBoarding = prefs.participationRoomId.isBlank()
        val isRoomMeetNewUser = room.participants.all{ userId != it.userId }
        val isRoomMeetOldUser = room.participants.any{ userId == it.userId && !it.expired }
        val isRoomMeetExactUser = room.participants.any{ userId == it.userId && it.expired }

        if (userId == room.userId) onStateChange(ResourceState(room = room.copy(isOwner = true)))
        else getCurrentUser{ user ->
            val isUserIsFree = user.participants.all { it.expired }

            Log.d(TAG, "onDetailingRoom: isUserOffBoarding = $isUserOffBoarding")
            Log.d(TAG, "onDetailingRoom: isUserIsFree = $isUserIsFree")
            Log.d(TAG, "onDetailingRoom: isRoomMeetNewUser = $isRoomMeetNewUser")
            Log.d(TAG, "onDetailingRoom: isRoomMeetOldUser = $isRoomMeetOldUser")
            Log.d(TAG, "onDetailingRoom: isRoomMeetExactUser = $isRoomMeetExactUser") //ROM-2c2b752f-c45b-43b5-b1a0-13cab9d0c126

            /*D/RoomViewModel: onDetailingRoom: isUserOffBoarding = true
            D/RoomViewModel: onDetailingRoom: isUserIsFree = false
            D/RoomViewModel: onDetailingRoom: isRoomMeetNewUser = false
            D/RoomViewModel: onDetailingRoom: isRoomMeetOldUser = true
            D/RoomViewModel: onDetailingRoom: isRoomMeetExactUser = false*/

            onStateChange(ResourceState(
                room = room.copy(
                    isOwner = false,
                    isJoinAccepted = isUserOffBoarding && isUserIsFree && isRoomMeetNewUser,
                    isParticipated = isRoomMeetOldUser,
                    isParticipant = isRoomMeetExactUser
                )
            ))
        }

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

    /*private fun onResolveCreateMessaging(t: Throwable, keyName: String? = null) {
        Log.d(TAG, "onResolveCreateMessaging: $keyName ${t.localizedMessage}")

        if (keyName == null) return
        prefs.createMessaging = keyName

        t.localizedMessage?.let(::onShowSnackBar)
    }*/
    /*private fun onResolveAddMessaging(t: Throwable) {
        Log.d(TAG, "onResolveRegisterMessaging: ${t.message}")
        prefs.addMessaging = true

        t.localizedMessage?.let(::onShowSnackBar)
    }*/
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

    override fun onImpressed() { TODO("Not yet implemented") }
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
    override fun onReNavigateRoom(roomId: String) {
        onNavigateBack()
        onNavigateTo(Screen.Home.Summary.RoomDetail.routeWithArgs(roomId/*, IRoomEvent.OWN_IS_TRUE*/))
    }
    private val _isRoomAlarmUp = mutableStateOf(prefs.roomAlarm)
    val isRoomAlarmUp get() = _isRoomAlarmUp.value
    fun onIsAlarmUpChange(minute: Int, selected: Boolean){
        if (selected) caseRoom.alarmManager.setupMinute(minute)
        else caseRoom.alarmManager.cancel()

        _isRoomAlarmUp.value = selected
        prefs.roomAlarm = isRoomAlarmUp
    }
    /*override fun turnOnAlarm(minute: Int) =
        caseRoom.alarmManager.setupMinute(minute)
    override fun turnOffAlarm() {
        caseRoom.alarmManager.cancel()
    }*/
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
            onSubmitted = if(room.isJoinAccepted || room.isParticipated) ::onSubmit else null))
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
        enabled: Boolean,
        participant: Participant,
        setIsRefresh: (Boolean) -> Unit
    ) =
        onShowDialog(
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