package com.dudegenuine.whoknows.ui.presenter.participant

import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Participant
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.presenter.participant.contract.IParticipantViewModel
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
    private val postParticipantUseCase: PostParticipant,
    private val getParticipantUseCase: GetParticipant,
    private val patchParticipantUseCase: PatchParticipant,
    private val deleteParticipantUseCase: DeleteParticipant,
    private val getParticipantsUseCase: GetParticipants): BaseViewModel(), IParticipantViewModel {

    override fun initParticipant(participant: Participant) {
        _state.value = ResourceState(participant = participant)
    }

    override fun postParticipant(participant: Participant) {
        if (participant.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        participant.apply { createdAt = Date() }

        postParticipantUseCase(participant)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getParticipant(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getParticipantUseCase(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchParticipant(id: String, current: Participant) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchParticipantUseCase(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteParticipant(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        deleteParticipantUseCase(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getParticipants(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getParticipantsUseCase(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}