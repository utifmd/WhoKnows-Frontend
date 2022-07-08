package com.dudegenuine.whoknows.ux.vm.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.work.ExistingWorkPolicy
import com.dudegenuine.model.Impression
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Room
import com.dudegenuine.repository.contract.dependency.local.IResourceDependency
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.WORK_NAME_ROOM_TOKEN
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager.Companion.TOPIC_COMMON
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import com.dudegenuine.whoknows.ux.compose.state.NotificationState
import com.dudegenuine.whoknows.ux.vm.notification.contract.IMessagingViewModel.Companion.DEFAULT_NOTIFIER_BATCH_SIZE
import com.dudegenuine.whoknows.ux.vm.participation.contract.IParticipantViewModel.Companion.DEFAULT_PARTICIPANT_BATCH_SIZE
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel.Companion.DEFAULT_BATCH_ROOM
import com.dudegenuine.whoknows.ux.vm.user.contract.IUserViewModel.Companion.DEFAULT_BATCH_PARTICIPANT
import com.dudegenuine.whoknows.ux.worker.TokenWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseNotifier: INotificationUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val caseRoom: IRoomUseCaseModule,
    private val caseImpression: IImpressionUseCaseModule,

    caseQuiz: IQuizUseCaseModule,
    savedStateHandle: SavedStateHandle): IMainViewModel() {
    private val TAG = javaClass.simpleName
    val userId get() = prefs.userId
    val receiver get() = caseMessaging.receiver
    val isLoggedIn get() = prefs.userId.isNotBlank()
    val isParticipated get() = prefs.participationRoomId

    @Inject lateinit var resource: IResourceDependency

    private val messaging get() = caseMessaging.firebase.messaging()
    private val worker get() = caseMessaging.workerManager.instance()
    private val prefs get() = caseUser.preferences

    private val _userIndicator = MutableStateFlow<String?>(null)
    private val userIndicator get() = _userIndicator
    fun onUserIndicatorChange(
        userId: String?) = viewModelScope.launch {
        Log.d(TAG, "onUserIndicatorChange: $userId")
        _userIndicator.emit(userId)
    }
    init {
        messagingSubscribeTopic()
        //initialFeedState()
        //userSubscribeAuthenticated()
        if (prefs.tokenId.isBlank()) messagingInitToken()
        else Log.d(TAG, "currentToken: ${prefs.tokenId}")

        if (isLoggedIn) getUser()
    }
    val roomCompleteFlow = userIndicator
        .flatMapLatest{ caseRoom.getRooms(it ?: userId, DEFAULT_BATCH_ROOM) }
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
    val notificationsFlow = userIndicator
        .flatMapLatest{ caseNotifier.getNotifications(it ?: userId, DEFAULT_NOTIFIER_BATCH_SIZE) }
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
    val roomsCensoredFlow = userIndicator
        .flatMapLatest{ caseRoom.getRooms(DEFAULT_BATCH_ROOM) }
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
    val participantsFlow = caseUser
        .getUsersParticipation(DEFAULT_BATCH_PARTICIPANT)
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
    val questionsFlow = caseQuiz
        .getQuestions(DEFAULT_PARTICIPANT_BATCH_SIZE)
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    val connectionReceiver = receiver.connectionReceiver { message ->
        if (message.isNotBlank()) if(prefs.tokenId.isBlank()) messagingInitToken()
        onShowSnackBar(message)
    }
    val messagingReceiver = receiver.messagingReceiver { token ->
        val serverRequest = TokenWorker.oneTimeBuilder(token).build()
        val chainer = worker.beginUniqueWork(
            WORK_NAME_ROOM_TOKEN,
            ExistingWorkPolicy.KEEP,
            serverRequest
        )
        token.let(::onTokenIdChange)
        if (isLoggedIn) chainer.enqueue() /*.then(onPreRegisterMessaging)*/
    }

    // TODO: please do magic with worker here
    private fun messagingSubscribeTopic() {
        messaging.apply {
            isAutoInitEnabled = true
            subscribeToTopic(TOPIC_COMMON)
        }
    }
    private fun messagingInitToken() {
        with (messaging.token) {
            addOnSuccessListener {
                Log.d(TAG, "Success: $it")
                onTokenIdChange(it)
            }
            addOnCanceledListener {
                Log.d(TAG, "Canceled: triggered") }
            addOnFailureListener {
                Log.d(TAG, "Failure: ${it.localizedMessage}")
            }
        }
    }
    private fun getUser(){
        caseUser.getUser()
            .onEach(::onAuth)
            .onCompletion{ if(it == null) Log.d(TAG, "getUser: complete") }
            .launchIn(viewModelScope)
    }
    fun onImpression(impressed: Boolean, room: Room.Censored, onFinish: () -> Unit){
        val actor = auth.user?.fullName?.ifBlank{ resource.string(R.string.unknown) }
            ?: auth.user?.username ?: resource.string(R.string.unknown)
        val event = "$actor just like the ${room.title} class"
        val pusher = Messaging.Pusher(
            title = actor,
            body = event,
            largeIcon = auth.user?.profileUrl ?: EMPTY_STRING,
            to = room.token
        )
        val notification = NotificationState().copy(
            userId = userId,
            roomId = room.roomId,
            event = event,
            recipientId = room.userId
        )
        val impression = Impression(
            impressionId = "IMP-${UUID.randomUUID()}",
            postId = room.roomId,
            userId = userId,
            good = impressed
        )
        Log.d(TAG, "onImpression: $impressed")
        caseImpression.operateImpression(impressed, room, notification, impression, pusher)
            .onCompletion{ onFinish() }
            .launchIn(viewModelScope)
    }
    private fun onTokenIdChange(fresh: String) {
        prefs.tokenId = fresh
    }
}