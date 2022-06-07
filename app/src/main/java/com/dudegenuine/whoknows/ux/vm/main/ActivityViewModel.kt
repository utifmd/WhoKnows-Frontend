package com.dudegenuine.whoknows.ux.vm.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import com.dudegenuine.model.User
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.NOTIFICATION_BADGE
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.USER_ID
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.WORK_NAME_ROOM_TOKEN
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager.Companion.TOPIC_COMMON
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.worker.TokenWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ActivityViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseNotifier: INotificationUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    savedStateHandle: SavedStateHandle): IActivityViewModel() {
    private val TAG = javaClass.simpleName
    private val userId get() = prefs.userId
    val receiver get() = caseMessaging.receiver

    private val messaging get() = caseMessaging.firebase.messaging()
    private val worker get() = caseMessaging.workerManager.instance()
    private val prefs get() = caseUser.preferences
    
    //private val _currentUserId = mutableStateOf(userId)
    val currentUserId get() = prefs.userId //_currentUserId.value
    val isLoggedIn get() = currentUserId.isNotBlank()

    init {
        messagingSubscribeTopic()

        if (prefs.tokenId.isBlank()) messagingInitToken()
        else Log.d(TAG, "currentToken: ${prefs.tokenId}")

        if (isLoggedIn) getUser()
        //getNotifications(prefs.userId)
    }

    // TODO: next merge and collect these sample of original cases 
    /*private var rooms by mutableStateOf(List(5){ RoomState.room.copy(title = "Judul Room #$it") })
    val pagingFlow: Flow<PagingData<Any>> = flow {
        emit(PagingData.from(listOf("Hari ini")))
        emit(PagingData.from(listOf(2017)))
        emit(PagingData.from(listOf("Minggu ini")))
        emit(PagingData.from(rooms))
        emit(PagingData.from(listOf(2001)))
        emit(PagingData.from(listOf("Bulan lalu")))
        emit(PagingData.from(listOf(1994)))
        emit(PagingData.from(rooms))
    }*/

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

    /*private var savedPagingData by mutableStateOf<PagingData<Quiz.Complete>?>(null)

    val feedRoomFlow = caseRoom.getRooms(prefs.userId, 3)
        .cachedIn(viewModelScope)

    val feedParticipantFlow = caseUser.getUsersParticipation(3)
        .cachedIn(viewModelScope)

    val feedQuizFlow = caseQuiz.getQuestions(3)
        .cachedIn(viewModelScope)*/

    fun registerPrefsListener() { prefs.manager.register(onPrefsListener) }
    fun unregisterPrefsListener() { prefs.manager.unregister(onPrefsListener) }

    private val onPrefsListener = SharedPreferences
        .OnSharedPreferenceChangeListener { prefs, key -> when (key) {
            USER_ID -> {
                val fresh = prefs?.getString(USER_ID, "")

                fresh?.let(::onCurrentUserIdChange)
            }
            NOTIFICATION_BADGE -> {
                val fresh = prefs?.getInt(NOTIFICATION_BADGE, 0)

                fresh?.let(::onBadgeChange)
            }
        }
    }

    private fun getNotifications(userId: String){
        if (userId.isBlank()) return

        caseNotifier.getNotifications(userId, 0, Int.MAX_VALUE)
            .onEach { res -> onResourceStateless(res) { notifiers ->
                val counter = notifiers.count { !it.seen }
                onBadgeChange(counter)
            }}
            .launchIn(viewModelScope)
    }

    private fun getUser(){
        caseUser.getUser()
            .onEach(::onStore)
            .onCompletion { if(it == null) Log.d(TAG, "getUser: complete") }
            .launchIn(viewModelScope)
    }

    private fun getUser(onSucceed: (User.Complete) -> Unit){
        caseUser.getUser(currentUserId)
            .onEach { onResourceSucceed(it, onSucceed) }
            .launchIn(viewModelScope)
    }

    private fun onBadgeChange(fresh: Int) =
        onStateChange(ResourceState(badge = fresh))
    /*badge = fresh prefs.notificationBadge = fresh*/

    private fun onCurrentUserIdChange(fresh: String) {
        //_currentUserId.value = fresh
        prefs.userId = fresh
    }

    private fun onTokenIdChange(fresh: String) {
        prefs.tokenId = fresh
    }

    /*private fun onRejectCreateMessaging() {
        prefs.addMessaging = false
    }

    private fun onRejectAddMessaging() {
        prefs.addMessaging = false
    }

    private fun onRejectRemoveMessaging() {
        prefs.removeMessaging = false
    }*/

    /*private var _fruitList = mutableListOf("grape", "apple", "orange", "melon", "water melon", "cherry", "papaya", "pineapple")
    private val fruitList get() = _fruitList

    private val source: PagingSource<Int, String> = ResourcePaging {
        delay(2_000)
        fruitList
    }

    private val pager = Pager(PagingConfig(
        2, enablePlaceholders = true, maxSize = 200
    ), pagingSourceFactory = { source })

    val feedFruits: () -> Flow<PagingData<String>> = { pager.flow }


    fun onFruitsAdded(fruit: String){
        _fruitList.add(0, fruit)

        Log.d(TAG, "onFruitsAdded: $fruit")
    }

    fun onFruitsRemoved(index: Int){
        _fruitList.removeAt(index)

        Log.d(TAG, "onFruitsRemoved: $index")
    }*/
}