package com.dudegenuine.whoknows.ui.view.result

import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Result
import com.dudegenuine.usecase.result.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.result.contract.IResultViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
class ResultViewModel
    @Inject constructor(
    private val postResultUseCase: PostResult,
    private val getResultUseCase: GetResult,
    private val patchResultUseCase: PatchResult,
    private val deleteResultUseCase: DeleteResult,
    private val getResultsUseCase: GetResults
): BaseViewModel(), IResultViewModel {

    override fun postResult(result: Result) {
        postResultUseCase(result)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getResult(id: String) {
        getResultUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchResult(id: String, current: Result) {
        patchResultUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteResult(id: String) {
        deleteResultUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getResults(page: Int, size: Int) {
        getResultsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}