package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IShareLauncher
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ux.vm.file.IFileViewModel
import com.dudegenuine.whoknows.ux.vm.main.IMainViewModel
import com.dudegenuine.whoknows.ux.vm.notification.contract.INotificationViewModel
import com.dudegenuine.whoknows.ux.vm.participation.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ux.vm.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ux.vm.user.contract.IUserViewModel

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IViewModelModule {
    fun provideMainActivityViewModel(
        messagingUseCaseModule: IMessageUseCaseModule,
        notifier: INotificationUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        roomUseCaseModule: IRoomUseCaseModule,
        quizUseCaseModule: IQuizUseCaseModule,
        caseImpression: IImpressionUseCaseModule,
        savedStateHandle: SavedStateHandle): IMainViewModel

    fun provideUserViewModel(
        prefsFactory: IPrefsFactory,
        userUseCase: IUserUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle): IUserViewModel

    fun provideRoomViewModel(
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
        roomUseCaseModule: IRoomUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): IParticipantViewModel

    fun provideNotificationViewModel(
        prefsFactory: IPrefsFactory,
        case: INotificationUseCaseModule,
        savedStateHandle: SavedStateHandle): INotificationViewModel

    fun provideFileViewModel(
        case: IFileUseCaseModule,
        launcher: IShareLauncher,
        savedStateHandle: SavedStateHandle): IFileViewModel
}