package com.dudegenuine.whoknows.ui.presenter.quiz

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.KeyEvent
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.*
import com.dudegenuine.model.common.Utility.strOf
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

    val resourceState: State<ResourceState> = _state

    val currentQuestion = mutableStateOf("")
    val currentOption = mutableStateOf("")

    val currentAnswer = mutableStateOf<Answer?>(null)
    val selectedAnswer = mutableStateOf<PossibleAnswer?>(null)

    val images = mutableStateListOf<Uri>()
    val options = mutableStateListOf<String>()

    private val setOfAnswers = mutableSetOf<String>()

    val isValid: MutableState<Boolean>
        get() = mutableStateOf(
            selectedAnswer.value != null &&
                    currentQuestion.value.isNotBlank() &&
                    images.isNotEmpty() &&
                    options.isNotEmpty()
        ) // init { getQuestions(0, 10) }

    val onResultImage: (Context, Uri?) -> Unit = { context, result ->
        result?.let { uri ->
            // val item = context.contentResolver.openInputStream(uri)
            // val bitmap = BitmapFactory.decodeStream(item)

            if(!images.contains(uri)) images.add(uri)

            // item?.close()
        }
    }

    val onOptionKeyEvent: (KeyEvent) -> Boolean = {
        if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER)
            onPushedOption()
        false
    }

    val onPushedOption: () -> Unit = {
        if (currentOption.value.isNotBlank()){
            options.add(currentOption.value).apply {
                currentOption.value = ""
            }
        }
    }

    val onAnsweredSingle: (String) -> Unit = { newAnswer ->
        selectedAnswer.value = PossibleAnswer.SingleChoice(newAnswer)
    }

    val onAnsweredMultiple: (String, Boolean) -> Unit = { newAnswer, selected ->
        if (selected) setOfAnswers.add(newAnswer)
        else setOfAnswers.remove(newAnswer)

        selectedAnswer.value = PossibleAnswer.MultipleChoice(setOfAnswers)
    }

    fun onPostPressed () {
        if (images.isEmpty()) return

        val model = Quiz(
            "QIZ-${UUID.randomUUID()}",
            "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda",
            images = emptyList(), //.map { asBase64(it) },
            question = currentQuestion.value,
            options = options.toList(),
            answer = selectedAnswer.value,
            createdBy = "Diyanti Ratna Puspita Sari",
            createdAt = Date(),
            updatedAt = null
        )
        _state.value = ResourceState(quiz = model)

        uploadFile(images[0]) //images.map { }
    }

    override fun uploadFile(uri: Uri?) {
        if (uri == null) return

        uploadFileUseCase(uri)
            .onEach(this::onFileUploaded).launchIn(viewModelScope)
    }

    private fun onFileUploaded(resource: Resource<File>) {
        val model = _state.value.quiz

        when(resource){
            is Resource.Success -> {
                val downloadedUrl = resource.data?.url ?: ""

                model?.apply {
                    images = listOf(downloadedUrl)

                    Log.d(TAG, "onFileUploaded: $downloadedUrl")

                    postQuiz(this)
                }

                Log.d(TAG, model.toString())
            }
            is Resource.Error -> _state.value = ResourceState(
                error = resource.message ?: "An unexpected error occurred.")
            is Resource.Loading -> _state.value = ResourceState(
                loading = true)
        }
    }

    override fun postQuiz(quiz: Quiz) {
        if (quiz.roomId.isBlank() || quiz.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        postQuizUseCase(quiz)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchQuizUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        deleteQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuestions(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getQuestionsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}