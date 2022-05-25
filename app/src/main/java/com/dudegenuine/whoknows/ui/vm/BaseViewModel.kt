package com.dudegenuine.whoknows.ui.vm

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.*
import com.dudegenuine.whoknows.ui.compose.state.DialogState
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
    val jobScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /*private val eventChannel = Channel<String>(
        capacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val eventFlow = eventChannel.receiveAsFlow()

    fun sendEvent(element: String) = eventChannel.trySend(element)*/

    /*private val _eventState = mutableStateOf<EventState?>(null)
    val eventState = _eventState.value*/

    private val _state = mutableStateOf(ResourceState())
    val state: ResourceState
        get() = _state.value
    val stateResource: State<ResourceState> get() = _state

    private val _authState = mutableStateOf(ResourceState.Auth())
    val authState: ResourceState.Auth
        get() = _authState.value

    private val _snackMessage = MutableSharedFlow<String>()
    val snackMessage = _snackMessage.asSharedFlow()

    private val _dialogContent = MutableSharedFlow<DialogState?>()
    val dialogContent = _dialogContent.asSharedFlow()

    fun onStateChange(fresh: ResourceState) = viewModelScope.launch {
        _state.value = fresh

        if(fresh.error.isNotBlank()) onShowSnackBar(fresh.message)
    }

    fun onAuthStateChange(fresh: ResourceState.Auth){
        _authState.value = fresh
    }

    fun onShowSnackBar(message: String){
        viewModelScope.launch {
            _snackMessage.emit(message)
        }
    }

    fun onShowDialog(content: DialogState?) {
        viewModelScope.launch {
            _dialogContent.emit(content)
        }
    }

    fun onFlowFailed(methodName: String?, t: Throwable){
        t.localizedMessage?.let(::onShowSnackBar)
        //Toast.makeText(this, "", Toast.LENGTH_LONG).show()
        Log.d(TAG, "${methodName ?: "onCompletionFlowFailed"}: ${t.message}")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    protected fun<T> onResource(resource: Resource<T>){
        onResourceSucceed(resource){ data ->
            if (data is List<*>) {
                val payload = data as List<*>

                val initialState = ResourceState(
                    users = payload.filterIsInstance<User.Complete>(),
                    rooms = payload.filterIsInstance<Room.Complete>(),
                    questions = payload.filterIsInstance<Quiz.Complete>(),
                    results = payload.filterIsInstance<Result>(),
                    participants = payload.filterIsInstance<Participant>(),
                    notifications = payload.filterIsInstance<Notification>(),
                    files = payload.filterIsInstance<File>(),
                )

                _state.value = if(payload.isEmpty())
                    ResourceState(error = "No result.")
                else initialState

            } /*else if (data is PagingData<*>) {
                val payload = data as PagingData<*>
                val pagingData = payload.
                val initialState = ResourceState(
                    pagedRooms =
                )
            } */else _state.value = when(data) {
                is User.Complete -> ResourceState(user = data as User.Complete)
                is User.Censored -> ResourceState(userCensored = data as User.Censored)
                is Room.Complete -> ResourceState(room = data as Room.Complete)
                is Room.Censored -> ResourceState(roomCensored = data as Room.Censored)
                is Quiz.Complete -> ResourceState(quiz = data as Quiz.Complete)
                is Participant -> ResourceState(participant = data as Participant)
                is Notification -> ResourceState(notification = data as Notification)
                is Result -> ResourceState(result = data as Result)
                is File -> ResourceState(file = data as File)
                is String -> ResourceState(message = data)
                else -> ResourceState()
            }

            Log.d(TAG, "Resource.Success")
        }
    }

    protected fun<T> onResourceSucceed(
        resources: Resource<T>, onSuccess: (T) -> Unit){

        when(resources){
            is Resource.Success -> resources.data?.let { onSuccess.invoke(it) } ?: also {
                Log.d(TAG, "Resource.Error: ${resources.message}")
                _state.value = ResourceState(
                    error = resources.message ?: "Resource data is null."
                )
            }

            is Resource.Error -> {
                Log.d(TAG, "Resource.Error: ${resources.message}")
                _state.value = ResourceState(
                    error = resources.message ?: "An unexpected error occurred."
                )

                resources.message?.let(::onShowSnackBar)
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.Loading..")
                _state.value = ResourceState(loading = true)

                resources.data?.let {
                    Log.d(TAG, "Resource.Loading.. already got data")
                    onSuccess(it) //resources.data?.let()
                    _state.value = ResourceState(loading = false)
                }
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
            is Resource.Error -> resources.message?.let {
                onError(it)
                ::onShowSnackBar
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.Loading..")
                _state.value = ResourceState(loading = true)

                resources.data?.let {
                    Log.d(TAG, "Resource.Loading.. already got data")
                    onSuccess(it) //resources.data?.let()
                    _state.value = ResourceState(loading = false)
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
                //_state.value = ResourceState(loading = true)

                resources.data?.let {
                    Log.d(TAG, "onResourceFLow: loading already got data")
                    //_state.value = ResourceState(loading = false)
                    onSucceed.invoke(it)
                } ?: emptyFlow()
            }
            is Resource.Error -> {
                Log.d(TAG, "onResourceFlowError: ${resources.message}")
                //_state.value = ResourceState(loading = false)
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
                    if (data is User.Complete) _state.value = ResourceState(
                        user = data as User.Complete
                    )

                    onSucceed.invoke(data) } ?: emptyFlow()
            }
            is Resource.Loading -> {
                Log.d(TAG, "onResourceAuthFlow: loading..")
                _authState.value = ResourceState.Auth(loading = true)

                resources.data?.let {
                    Log.d(TAG, "onResourceAuthFlow: loading already got data")
                    _authState.value = ResourceState.Auth(loading = false)
                    onSucceed.invoke(it)
                } ?: emptyFlow()
            }
            is Resource.Error -> {
                Log.d(TAG, "onResourceFlowError: ${resources.message}")
                _authState.value = ResourceState.Auth(error = resources.message ?: "Invalid authentication")

                resources.message?.let(::onShowSnackBar)
                emptyFlow()
            }
        }
    }

    protected fun<T> onAuth(resources: Resource<T>, onSucceed: (User.Complete) -> Unit) {
        onAuth(resources, onSucceed) {

            _authState.value = ResourceState.Auth()
        }
    }

    protected fun<T> onAuth(
        resources: Resource<T>, onSucceed: ((User.Complete) -> Unit)? = null, onSignedOut: (() -> Unit)? = null){
        when(resources){
            is Resource.Success -> {

                _state.value = when(resources.data) {
                    is User.Complete -> (resources.data as User.Complete).let {
                        onSucceed?.invoke(it)

                        ResourceState(user = it)
                    }

                    is String -> {
                        onSignedOut?.invoke()

                        ResourceState( // user signed out
                            user = null,
                            error = resources.message ?: "An unexpected error occurred."
                        )
                    }

                    else -> ResourceState()
                }
            }

            is Resource.Error -> {
                Log.d(TAG, "Auth.Error: ${resources.message}")
                _authState.value = ResourceState.Auth(
                    error = resources.message ?: "An unexpected error occurred."
                )

                resources.message?.let(::onShowSnackBar)
            }
            is Resource.Loading -> {
                Log.d(TAG, "Auth.Loading..")
                _authState.value = ResourceState.Auth(
                    loading = true
                )
            }
        }
    }
}