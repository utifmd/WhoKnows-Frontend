package com.dudegenuine.whoknows.ui.vm.quiz

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dudegenuine.model.File
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IQuizUseCaseModule
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizState.Companion.QUIZ_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_OWNER_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.state.QuizState
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.contract.IQuizViewModel.Companion.BATCH_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
class QuizViewModel
    @Inject constructor(
    private val caseQuiz: IQuizUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IQuizViewModel {
    private val TAG: String = strOf<QuizViewModel>()

    private val resourceState: ResourceState
        get() = _state.value

    private val _formState = mutableStateOf(QuizState.FormState())
    val formState: QuizState.FormState
        get() = _formState.value

    init {
        savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)
            ?.let(formState::onRoomIdValueChange)

        savedStateHandle.get<String>(ROOM_OWNER_SAVED_KEY)
            ?.let(formState::onUserIdValueChange)

        savedStateHandle.get<String>(QUIZ_ID_SAVED_KEY)
            ?.let(this::getQuiz)
    }

    fun onPostPressed(onSucceed: (Quiz) -> Unit) {
        val model = formState.postModel
        Log.d(TAG, "onPostPressed: triggered")

        if (formState.isValid) {
            if (formState.images.isNotEmpty())
                multiUpload(formState.images, onSucceed)
            else postQuiz(model, onSucceed)

        } else {
            _state.value = ResourceState(error = DONT_EMPTY)
        }
    }

    override fun <T> multiUpload(byteArrays: List<ByteArray>, onSucceed: (T) -> Unit) {
        if (byteArrays.isEmpty()) return

        caseFile.uploadFiles(byteArrays)
            .onEach { onMultiUploaded(it, onSucceed) }.launchIn(viewModelScope)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> onMultiUploaded(resources: Resource<List<File>>, onSucceed: (T) -> Unit) {
        val model = resourceState.quiz ?: formState.postModel
        Log.d(TAG, "onFileUploaded: $model")

        onResourceSucceed(resources) { data ->
            val downloadedUrls = data.map { it.url }
            Log.d(TAG, "Resource.Success $downloadedUrls")

            model.apply { images = downloadedUrls }

            postQuiz(model) { onSucceed(it as T) }
            Log.d(TAG, "onFileUploaded: $model")
        }
    }

    private fun postQuiz(quiz: Quiz, onSucceed: (Quiz) -> Unit) {
        if (quiz.roomId.isBlank() || quiz.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseQuiz.postQuiz(quiz)
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun postQuiz(quiz: Quiz) {
        if (quiz.roomId.isBlank() || quiz.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseQuiz.postQuiz(quiz)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseQuiz.getQuiz(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        caseQuiz.patchQuiz(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseQuiz.deleteQuiz(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    val questions = caseQuiz.getQuestions(BATCH_SIZE)
        .cachedIn(viewModelScope)

    override fun getQuestions(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseQuiz.getQuestions(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    /*override fun singleUpload(byteArray: ByteArray) {
        if (byteArray.isEmpty()) return

        caseFile.uploadFile(byteArray)
            .onEach(this::onSingleUploaded).launchIn(viewModelScope)
    }
    override fun onSingleUploaded(resource: Resource<File>) {
        val model = formState.value.model.value

        Log.d(TAG, "onFileUploaded: $model")
        onUploaded(resource){
            Log.d(TAG, "Resource.Success: ${resource.data}") // val downloadedUrl = resource.data?.url ?: return

            if(resource.data?.url == null) {
                Log.d(TAG, "onFileUploaded: url null")
                _state.value = ResourceState(error = NULL_STATE)
            }

            val uploadedUrl = resource.data?.url ?: return@onUploaded

            model.apply { images = listOf(uploadedUrl) }

            Log.d(TAG, "onFileUploaded: $model")
            postQuiz(model)
        }
    }*/
}