package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ui.presenter.participant.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ui.presenter.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ui.presenter.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ui.presenter.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.presenter.user.contract.IUserViewModel

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IViewModelModule {
    fun provideUserViewModel(
        mapper: IUserDataMapper,
        userUseCase: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): IUserViewModel

    fun provideRoomViewModel(
        roomUseCaseModule: IRoomUseCaseModule,
        userUseCaseModule: IUserUseCaseModule, /*mapper: IUserDataMapper,*/
        savedStateHandle: SavedStateHandle): IRoomViewModel

    fun provideQuizViewModel(
        quizUseCaseModule: IQuizUseCaseModule,
        fileUseCaseModule: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle): IQuizViewModel

    fun provideResultViewModel(
        userUseCaseModule: IUserUseCaseModule,
        resultUseCaseModule: IResultUseCaseModule,
        savedStateHandle: SavedStateHandle): IResultViewModel

    fun provideParticipantViewModel(
        participantUseCaseModule: IParticipantUseCaseModule,
        savedStateHandle: SavedStateHandle): IParticipantViewModel
}