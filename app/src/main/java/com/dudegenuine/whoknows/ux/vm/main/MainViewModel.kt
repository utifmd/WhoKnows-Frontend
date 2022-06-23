package com.dudegenuine.whoknows.ux.vm.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.ExistingWorkPolicy
import com.dudegenuine.model.Room
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.WORK_NAME_ROOM_TOKEN
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager.Companion.TOPIC_COMMON
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ux.compose.state.room.FlowParameter
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

    caseQuiz: IQuizUseCaseModule,
    savedStateHandle: SavedStateHandle): IMainViewModel() {
    private val TAG = javaClass.simpleName
    val userId get() = prefs.userId
    val receiver get() = caseMessaging.receiver
    val isLoggedInByPrefs get() = prefs.userId.isNotBlank()

    private val messaging get() = caseMessaging.firebase.messaging()
    private val worker get() = caseMessaging.workerManager.instance()
    private val prefs get() = caseUser.preferences

    private val _roomCompleteParameter = MutableStateFlow<FlowParameter>(FlowParameter.Nothing)
    private val roomCompleteParameter get() = _roomCompleteParameter
    fun onRoomCompleteParameterChange(parameter: FlowParameter){
        viewModelScope.launch {
            _roomCompleteParameter.emit(parameter)
        }
    }
    private val _notificationParameter = MutableStateFlow<FlowParameter>(FlowParameter.Nothing)
    private val notificationParameter get() = _notificationParameter
    fun onNotificationParameterChange(parameter: FlowParameter){
        viewModelScope.launch {
            _notificationParameter.emit(parameter)
        }
    }
    init {
        messagingSubscribeTopic()
        //userSubscribeAuthenticated()
        if (prefs.tokenId.isBlank()) messagingInitToken()
        else Log.d(TAG, "currentToken: ${prefs.tokenId}")

        if (isLoggedInByPrefs) getUser()
    }

    val roomCompleteFlow = roomCompleteParameter
        .flatMapLatest(::onRoomCompleteByUserIdFlow)
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
    val notificationsFlow = notificationParameter
        .flatMapLatest(::onNotificationByUserIdFlow)
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
    val roomsCensoredFlow = caseRoom
        .getRooms(DEFAULT_BATCH_ROOM)
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

    private fun onRoomCompleteByUserIdFlow(
        params: FlowParameter): Flow<PagingData<Room.Complete>> = when(params){
        is FlowParameter.RoomComplete -> caseRoom.getRooms(params.userId, DEFAULT_BATCH_ROOM) //.flatMapMerge{ flowOf(PagingData.from(params.list)) }
        else -> emptyFlow()
    }
    private fun onNotificationByUserIdFlow(params: FlowParameter) = when(params){
        is FlowParameter.Notification -> caseNotifier.getNotifications(userId, DEFAULT_NOTIFIER_BATCH_SIZE)
        else -> emptyFlow()
    }

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
        if (isLoggedInByPrefs) chainer.enqueue() /*.then(onPreRegisterMessaging)*/
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
    private fun onTokenIdChange(fresh: String) {
        prefs.tokenId = fresh
    }
}