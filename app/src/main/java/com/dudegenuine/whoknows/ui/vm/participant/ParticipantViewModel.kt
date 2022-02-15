package com.dudegenuine.whoknows.ui.vm.participant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Participant
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IParticipantUseCaseModule
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.participant.contract.IParticipantViewModel
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
class ParticipantViewModel

    @Inject constructor(
    private val case: IParticipantUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IParticipantViewModel {

    override fun initParticipant(participant: Participant) {
        _state.value = ResourceState(participant = participant)
    }

    override fun postParticipant(participant: Participant) {
        if (participant.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        participant.apply { createdAt = Date() }

        case.postParticipant(participant)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getParticipant(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getParticipant(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchParticipant(id: String, current: Participant) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        case.patchParticipant(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteParticipant(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.deleteParticipant(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getParticipants(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getParticipants(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}