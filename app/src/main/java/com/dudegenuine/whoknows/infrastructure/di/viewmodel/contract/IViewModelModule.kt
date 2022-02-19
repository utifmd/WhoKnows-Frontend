package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ui.vm.notification.contract.INotificationViewModel
import com.dudegenuine.whoknows.ui.vm.participant.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ui.vm.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IViewModelModule {
    fun provideMainActivityViewModel(
        messagingUseCaseModule: IMessageUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): IActivityViewModel

    fun provideUserViewModel( /*mapper: IUserDataMapper,*/
        userUseCase: IUserUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle): IUserViewModel

    fun provideRoomViewModel(
        caseRoom: IRoomUseCaseModule,
        caseUser: IUserUseCaseModule,
        caseParticipant: IParticipantUseCaseModule,
        caseMessagig: IMessageUseCaseModule,
        caseFile: IFileUseCaseModule,
        caseResult: IResultUseCaseModule,
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

    fun provideNotificationViewModel(
        case: INotificationUseCaseModule,
        savedStateHandle: SavedStateHandle): INotificationViewModel
}