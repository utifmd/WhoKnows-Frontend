package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ui.vm.file.IFileViewModel
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
        prefsFactory: IPrefsFactory,
        messagingUseCaseModule: IMessageUseCaseModule,
        notifier: INotificationUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): IActivityViewModel

    fun provideUserViewModel( /*mapper: IUserDataMapper,*/
        prefsFactory: IPrefsFactory,
        messaging: IMessageUseCaseModule,
        userUseCase: IUserUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle): IUserViewModel

    fun provideRoomViewModel(
        prefsFactory: IPrefsFactory,
        caseFile: IFileUseCaseModule,
        caseRoom: IRoomUseCaseModule,
        caseUser: IUserUseCaseModule,
        caseParticipant: IParticipantUseCaseModule,
        caseMessaging: IMessageUseCaseModule,
        caseQuiz: IQuizUseCaseModule,
        caseNotification: INotificationUseCaseModule,
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
        prefsFactory: IPrefsFactory,
        case: INotificationUseCaseModule,
        caseUser: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): INotificationViewModel

    fun provideFileViewModel(
        case: IFileUseCaseModule,
        launcher: IShareLauncher,
        savedStateHandle: SavedStateHandle): IFileViewModel
}