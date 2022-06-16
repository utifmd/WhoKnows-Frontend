package com.dudegenuine.whoknows.ux.vm

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.dudegenuine.model.*
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager.Companion.KEY_ROOM_TOKEN_RESULT
import com.dudegenuine.whoknows.ux.compose.model.Dialog
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class BaseViewModel: ViewModel() {
    private val TAG: String = javaClass.simpleName

    private val viewModelJob = SupervisorJob()
    val jobScope = CoroutineScope(Dispatchers.Main + viewModelJob) /*private val eventChannel = Channel<String>(capacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST) val eventFlow = eventChannel.receiveAsFlow() fun sendEvent(element: String) = eventChannel.trySend(element)*/

    private val _state = mutableStateOf(ResourceState())
    val state
        get() = _state.value
    private val _auth = mutableStateOf(ResourceState.Auth())
    val auth: ResourceState.Auth
        get() = _auth.value

    private val _screenState = MutableSharedFlow<ScreenState>()
    val screenState = _screenState.asSharedFlow()

    fun onStateChange(fresh: ResourceState) {
        _state.value = fresh
    }

    fun onAuthChange(fresh: ResourceState.Auth){
        Log.d(TAG, "onAuthChange: user ${fresh.user != null}")
        if (fresh.invalidated) {
            _auth.value = fresh
            return
        }
        _auth.value = if(fresh.user != null) fresh else auth.copy(
            loading = fresh.loading,
            error = fresh.error
        )
    }

    fun onScreenStateChange(state: ScreenState) {
        viewModelScope.launch {
            Log.d(TAG, "onScreenStateChange: $state")
            _screenState.emit(state)
        }
    }

    fun onToast(message: String) = onScreenStateChange(ScreenState.Toast(message))
    fun onShowSnackBar(message: String) = onScreenStateChange(ScreenState.SnackBar(message))
    fun onShowDialog(content: Dialog?) = onScreenStateChange(ScreenState.AlertDialog(content))
    fun onNavigateTo(route: String) = onScreenStateChange(ScreenState.Navigate.To(route))
    fun onNavigateBack() = onScreenStateChange(ScreenState.Navigate.Back)

    fun onFlowFailed(methodName: String?, t: Throwable){
        t.localizedMessage?.let { message ->
            onScreenStateChange(ScreenState.SnackBar(message))
        }
        Log.d(TAG, "${methodName ?: "onCompletionFlowFailed"}: ${t.message}")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    protected fun onWorkResult(info: WorkInfo?){
        when(info?.state){
            WorkInfo.State.SUCCEEDED -> {
                onScreenStateChange(ScreenState
                    .SnackBar("onWorkResult: SUCCEEDED"))

                if (info.state.isFinished){
                    val result = info.outputData.getString(KEY_ROOM_TOKEN_RESULT)

                    result?.let {
                        Log.d(TAG, "onWorkResult: result -> $it")
                        onShowDialog(Dialog("Work SUCCEEDED"))
                        onStateChange(ResourceState(workerOutput = it))
                    }
                }
            }
            WorkInfo.State.FAILED -> Log.d(TAG, "onWorkResult: FAILED")
            WorkInfo.State.CANCELLED -> Log.d(TAG, "onWorkResult: CANCELLED")
            WorkInfo.State.RUNNING -> Log.d(TAG, "onWorkResult: RUNNING")
            WorkInfo.State.ENQUEUED -> Log.d(TAG, "onWorkResult: ENQUEUED")
            WorkInfo.State.BLOCKED -> Log.d(TAG, "onWorkResult: BLOCKED")
            else -> Log.d(TAG, "onWorkResult: else")
        }
    }

    protected fun<T> onResource(resource: Resource<T>){
        onResourceSucceed(resource){ data ->
            Log.d(TAG, "onResource: Succeed")
            onStateChange(ResourceState(loading = false))
            when (data) {
                is List<*> -> {
                    val list = data as List<*>
                    if (list.isEmpty()) onStateChange(ResourceState(error = "No result."))
                    else onStateChange(ResourceState(
                        users = list.filterIsInstance<User.Complete>(),
                        rooms = list.filterIsInstance<Room.Complete>(),
                        questions = list.filterIsInstance<Quiz.Complete>(),
                        results = list.filterIsInstance<Result>(),
                        participants = list.filterIsInstance<Participant>(),
                        notifications = list.filterIsInstance<Notification>(),
                        files = list.filterIsInstance<File>(),
                    ))
                }
                is User.Complete -> {
                    val currentUser = data as User.Complete
                    onStateChange(ResourceState(user = currentUser))
                }
                is User.Censored -> onStateChange(ResourceState(userCensored = data as User.Censored))
                is Room.Complete -> {
                    Log.d(TAG, "onResource: Room.Complete ${data.id}")
                    onStateChange(ResourceState(room = data as Room.Complete))
                }
                is Room.Censored -> onStateChange(ResourceState(roomCensored = data as Room.Censored))
                is Quiz.Complete -> onStateChange(ResourceState(quiz = data as Quiz.Complete))
                is Participant -> onStateChange(ResourceState(participant = data as Participant))
                is Notification -> onStateChange(ResourceState(notification = data as Notification))
                is Result -> onStateChange(ResourceState(result = data as Result))
                is File -> onStateChange(ResourceState(file = data as File))
                is String -> onStateChange(ResourceState(message = data))
                else -> onStateChange(ResourceState())
            }
        }
    }

    protected fun<T> onResourceSucceed(
        resources: Resource<T>, onSuccess: (T) -> Unit){

        when(resources){
            is Resource.Success -> resources.data?.let {
                onSuccess.invoke(it)
            } ?: also {
                Log.d(TAG, "Resource.Error: ${resources.message}")
                onStateChange(ResourceState(error = resources.message ?: "Resource data is null."))
            }

            is Resource.Error -> {
                Log.d(TAG, "Resource.Error: ${resources.message}")
                onStateChange(ResourceState(error = resources.message ?: "An unexpected error occurred."))

                resources.message?.let { message ->
                    onScreenStateChange(ScreenState.SnackBar(message))
                }
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.Loading..")
                onStateChange(ResourceState(loading = true))
            }
        }
    }

    protected fun<T> onResourceBoarding(
        resources: Resource<T>, onPrepare: () -> Unit, onSuccess: (T) -> Unit){

        when(resources){
            is Resource.Success -> resources.data?.let(onSuccess)
            is Resource.Error -> resources.message?.let { onStateChange(ResourceState(error = it)) }
            is Resource.Loading -> onPrepare()
        }
    }
    protected fun<T> onResource(
        resources: Resource<T>, onSuccess: (T) -> Unit, onError: (String) -> Unit){

        when(resources){
            is Resource.Success -> resources.data?.let(onSuccess)
            is Resource.Error -> resources.message?.let { message ->
                onError(message)
                onScreenStateChange(ScreenState.SnackBar(message))
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.Loading..")
                onStateChange(ResourceState(loading = true))

                resources.data?.let {
                    Log.d(TAG, "Resource.Loading.. already got data")
                    onSuccess(it) //resources.data?.let()
                    onStateChange(ResourceState(loading = false))
                }
            }
        }
    }

    protected fun<T> onResourceStateless(resources: Resource<T>, onSucceed: ((T) -> Unit)? = null){
        if(resources is Resource.Success) {
            resources.data?.let { onSucceed?.invoke(it) }
            Log.d(TAG, "onResourceStateless: success")
        }

        if (resources is Resource.Loading){
            Log.d(TAG, "onResourceStateless: Loading..")

            resources.data?.let {
                Log.d(TAG, "onResourceStateless: Loading.. already got data")
                onSucceed?.invoke(it) //resources.data?.let()
            }
        }
        if (resources is Resource.Error){
            Log.d(TAG, "onResourceStateless: Error ${resources.message}")
            resources.message?.let { onScreenStateChange(ScreenState.Toast(it, Toast.LENGTH_LONG)) }
        }
    }

    protected fun<T> onResourceFlow(
        resources: Resource<T>, onSucceed: (T) -> Flow<Resource<T>>): Flow<Resource<T>> {
        return when(resources) {
            is Resource.Success -> {
                Log.d(TAG, "onResourceFLow: success")
                resources.data?.let { onSucceed.invoke(it) } ?: emptyFlow()
            }
            is Resource.Loading -> {
                Log.d(TAG, "onResourceFLow: loading..")

                resources.data?.let {
                    Log.d(TAG, "onResourceFLow: loading already got data")
                    onSucceed.invoke(it)
                } ?: emptyFlow()
            }
            is Resource.Error -> {
                Log.d(TAG, "onResourceFlowError: ${resources.message}")
                //onSavedStateChange(ResourceState(loading = false))
                resources.message?.let {
                    if (resources.data is User.Complete && it.contains("notification_key")) return@let
                    onScreenStateChange(ScreenState.Toast(it, Toast.LENGTH_LONG))
                }
                emptyFlow()
            }
        }
    }

    protected fun<T> onResourceAuthFlow(
        resources: Resource<T>, onSucceed: (T) -> Flow<Resource<T>>): Flow<Resource<T>> {
        return when(resources) {
            is Resource.Success -> {
                Log.d(TAG, "onResourceAuthFlow: success")
                resources.data?.let { data ->
                    if (data is User.Complete) onStateChange(ResourceState(user = data as User.Complete))
                    onSucceed.invoke(data) } ?: emptyFlow()
            }
            is Resource.Loading -> {
                Log.d(TAG, "onResourceAuthFlow: loading..")
                onStateChange(ResourceState(loading = true))

                resources.data?.let {
                    Log.d(TAG, "onResourceAuthFlow: loading already got data")
                    onStateChange(ResourceState(loading = false))
                    onSucceed.invoke(it)
                } ?: emptyFlow()
            }
            is Resource.Error -> {
                Log.d(TAG, "onResourceFlowError: ${resources.message}")
                onStateChange(ResourceState(error = resources.message ?: "Invalid authentication"))

                resources.message?.let { onScreenStateChange(ScreenState.SnackBar(it)) }
                emptyFlow()
            }
        }
    }
    protected fun<T> onAuth(
        resources: Resource<T>, onSucceed: ((User.Complete) -> Unit)? = null, onSignedOut: (() -> Unit)? = null){
        when(resources){
            is Resource.Success -> {

                when(resources.data) {
                    is User.Complete -> (resources.data as User.Complete).let {
                        onSucceed?.invoke(it)
                        onAuthChange(ResourceState.Auth(user = it))
                    }

                    is String -> {
                        onSignedOut?.invoke()
                        onAuthChange(ResourceState.Auth(invalidated = true)) // user signed out
                    }
                    else -> ResourceState.Auth(error = "unknown resources")
                }
            }

            is Resource.Error -> {
                Log.d(TAG, "Auth.Error: ${resources.message}")
                onAuthChange(ResourceState.Auth(error = resources.message ?: "An unexpected error occurred."))

                resources.message?.let(::onShowSnackBar)
            }
            is Resource.Loading -> {
                Log.d(TAG, "Auth.Loading..")
                onAuthChange(ResourceState.Auth(loading = true))
            }
        }
    }
}