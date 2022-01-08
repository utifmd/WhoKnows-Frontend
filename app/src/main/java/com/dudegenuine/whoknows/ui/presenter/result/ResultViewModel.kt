package com.dudegenuine.whoknows.ui.presenter.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Result
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IResultUseCaseModule
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.presenter.result.contract.IResultViewModel
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
    private val case: IResultUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IResultViewModel {

    override fun postResult(result: Result) {
        if (result.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        result.apply { createdAt = Date() }

        case.postResult(result)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getResult(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getResult(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchResult(id: String, current: Result) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        case.patchResult(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteResult(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.deleteResult(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getResults(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getResults(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}