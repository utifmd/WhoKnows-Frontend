package com.dudegenuine.whoknows.ux.vm.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import com.dudegenuine.model.Result
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IResultUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel.Companion.RESULT_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel.Companion.RESULT_USER_ID_SAVED_KEY
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
class ResultViewModel
    @Inject constructor(
    private val userCase: IUserUseCaseModule,
    private val case: IResultUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IResultViewModel {

    init {
        onResultRouted()
    }

    private fun onResultRouted() {
        val userId = savedStateHandle.get<String>(IResultViewModel.RESULT_USER_ID_SAVED_KEY)
        val roomId = savedStateHandle.get<String>(IResultViewModel.RESULT_ROOM_ID_SAVED_KEY)

        if (!userId.isNullOrBlank() && !roomId.isNullOrBlank()) getResult(roomId, userId)
    }

    override fun postResult(result: Result) {
        if (result.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        result.apply { createdAt = Date() }

        case.postResult(result)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getResult(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.getResult(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    private fun getResult(roomId: String, userId: String) {
        if (roomId.isBlank() or userId.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.getResult(roomId, userId)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchResult(id: String, current: Result) {
        if (id.isBlank() || current.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        current.apply { updatedAt = Date() }

        case.patchResult(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteResult(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.deleteResult(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getResults(page: Int, size: Int) {
        if (size == 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        case.getResults(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    fun onDonePressed() {
        val option = NavOptions.Builder().setPopUpTo(
            Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(
                "{$RESULT_ROOM_ID_SAVED_KEY}", "{$RESULT_USER_ID_SAVED_KEY}"), true).build()
        onScreenStateChange(ScreenState.Navigate.To(Screen.Home.Summary.route, option))
    }
}