package com.dudegenuine.whoknows.ui.presenter.quiz

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.input.key.KeyEvent
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.*
import com.dudegenuine.model.common.ImageUtil.adjustImage
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.usecase.file.UploadFile
import com.dudegenuine.usecase.quiz.*
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
    private val uploadFileUseCase: UploadFile,
    private val postQuizUseCase: PostQuiz,
    private val getQuizUseCase: GetQuiz,
    private val patchQuizUseCase: PatchQuiz,
    private val deleteQuizUseCase: DeleteQuiz,
    private val getQuestionsUseCase: GetQuestions): BaseViewModel(), IQuizViewModel {

    private val TAG: String = strOf<QuizViewModel>()
    private val resourceState: State<ResourceState> = _state

    private val _currentQuestion = mutableStateOf("")
    val currentQuestion: State<String> get() = _currentQuestion

    private val _currentOption = mutableStateOf("")
    val currentOption: State<String> get() = _currentOption

    private val _currentAnswer = mutableStateOf<Answer?>(null)
    val currentAnswer: State<Answer?> get() = _currentAnswer

    private val _selectedAnswer = mutableStateOf<PossibleAnswer?>(null)
    private val selectedAnswer: State<PossibleAnswer?> get() = _selectedAnswer

    private val _options = mutableStateListOf<String>()
    val options: SnapshotStateList<String> get() = _options

    private val _images = mutableStateListOf<ByteArray>()
    val images: SnapshotStateList<ByteArray> get() = _images

    private val setOfAnswers = mutableSetOf<String>()

    val isValid: MutableState<Boolean>
        get() = mutableStateOf(
            selectedAnswer.value != null &&
                    currentQuestion.value.isNotBlank() &&
                    images.isNotEmpty() &&
                    options.isNotEmpty()
        )

    val onResultImage: (Context, Uri?) -> Unit = { context, result ->
        result?.let { uri ->
            val scaledImage = adjustImage(context, uri)

            if(!images.contains(scaledImage)) images.add(scaledImage)
        }
    }

    val onOptionKeyEvent: (KeyEvent) -> Boolean = {
        if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER)
            onPushedOption()
        false
    }

    val onPushedOption: () -> Unit = {
        if (currentOption.value.isNotBlank()){
            options.add(currentOption.value.trim()).also {
                _currentOption.value = ""
            }
        }
    }

    val onQuestionValueChange: (String) -> Unit = {
        _currentQuestion.value = it
    }

    val onOptionValueChange: (String) -> Unit = {
        _currentOption.value = it
    }

    val onSelectedAnswerValue: (PossibleAnswer?) -> Unit = {
        _selectedAnswer.value = it
    }

    val onAnsweredSingle: (String) -> Unit = { newAnswer ->
        onSelectedAnswerValue(PossibleAnswer.SingleChoice(newAnswer))
    }

    val onAnsweredMultiple: (String, Boolean) -> Unit = { newAnswer, selected ->
        if (selected) setOfAnswers.add(newAnswer)
        else setOfAnswers.remove(newAnswer)

        onSelectedAnswerValue(PossibleAnswer.MultipleChoice(setOfAnswers))
    }

    fun onPostPressed () {
        val model = Quiz(
            "QIZ-${UUID.randomUUID()}",
            "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda",
            images = emptyList(),
            question = currentQuestion.value.trim(),
            options = options.toList(),
            answer = selectedAnswer.value,
            createdBy = "Diyanti Ratna Puspita Sari",
            createdAt = Date(),
            updatedAt = null
        )
        _state.value = ResourceState(quiz = model)

        if (isValid.value && resourceState.value.quiz != null)
            uploadFile(images[0])
        else
            _state.value = ResourceState(error = DONT_EMPTY)
    }

    override fun uploadFile(byteArray: ByteArray) {
        if (byteArray.isEmpty()) return

        uploadFileUseCase(byteArray)
            .onEach(this::onFileUploaded).launchIn(viewModelScope)
    }

    override fun onFileUploaded(resource: Resource<File>) {
        val model = resourceState.value.quiz ?: return

        Log.d(TAG, "onFileUploaded: $model")
        when(resource){
            is Resource.Success -> {
                Log.d(TAG, "Resource.Success: ${resource.data}")
                val downloadedUrl = resource.data?.url ?: return

                model.apply { images = listOf(downloadedUrl) }

                Log.d(TAG, "onFileUploaded: $model")
                postQuiz(model)
            }

            is Resource.Error -> {
                Log.d(TAG, "Resource.Error: ${resource.message}")
                _state.value = ResourceState(
                    error = resource.message ?: "An unexpected error occurred."
                )
            }
            is Resource.Loading -> {
                Log.d(TAG, "Resource.Loading..")
                _state.value = ResourceState(
                    loading = true
                )
            }
        }
    }

    override fun postQuiz(quiz: Quiz) {
        if (quiz.roomId.isBlank() || quiz.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        postQuizUseCase(quiz)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getQuizUseCase(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchQuizUseCase(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        deleteQuizUseCase(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getQuestions(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getQuestionsUseCase(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}