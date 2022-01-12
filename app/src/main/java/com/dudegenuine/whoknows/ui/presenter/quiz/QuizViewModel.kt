package com.dudegenuine.whoknows.ui.presenter.quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.File
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IQuizUseCaseModule
import com.dudegenuine.whoknows.ui.compose.state.QuizState
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.presenter.quiz.contract.IQuizViewModel
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

    private val resourceState: State<ResourceState>
        get() = _state

    private val _formState = mutableStateOf(QuizState())
    val formState: State<QuizState>
        get() = _formState

    fun onPostPressed () {
        val model = formState.value.model.value

        _state.value = ResourceState(quiz = model)

        if (formState.value.isValid.value && resourceState.value.quiz != null)
            multiUpload(formState.value.images)
        else
            _state.value = ResourceState(error = DONT_EMPTY)
    }

    override fun multiUpload(byteArrays: List<ByteArray>) {
        if (byteArrays.isEmpty()) return

        caseFile.uploadFiles(byteArrays)
            .onEach(this::onMultiUploaded).launchIn(viewModelScope)
    }

    override fun onMultiUploaded(resources: Resource<List<File>>) {
        val model = resourceState.value.quiz ?: formState.value.model.value
        Log.d(TAG, "onFileUploaded: $model")

        onUploaded(resources) { data ->
            val downloadedUrls = data.map { it.url }
            Log.d(TAG, "Resource.Success $downloadedUrls")

            model.apply { images = downloadedUrls }

            postQuiz(model)
            Log.d(TAG, "onFileUploaded: $model")
        }
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