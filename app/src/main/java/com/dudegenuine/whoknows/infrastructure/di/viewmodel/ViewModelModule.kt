package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import com.dudegenuine.usecase.participant.*
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.usecase.result.*
import com.dudegenuine.usecase.room.*
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ui.presenter.participant.ParticipantViewModel
import com.dudegenuine.whoknows.ui.presenter.participant.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.presenter.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ui.presenter.result.ResultViewModel
import com.dudegenuine.whoknows.ui.presenter.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel
import com.dudegenuine.whoknows.ui.presenter.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel
import com.dudegenuine.whoknows.ui.presenter.user.contract.IUserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule: IViewModelModule {

    @Provides
    @ViewModelScoped
    override fun provideUserViewModel(
        postUser: PostUser,
        getUser: GetUser,
        patchUser: PatchUser,
        deleteUser: DeleteUser,
        getUsers: GetUsers,
        signInUser: SignInUser //savedStateHandle: SavedStateHandle
    ): IUserViewModel {

        return UserViewModel(postUser, getUser, patchUser, deleteUser, getUsers, signInUser)
    }

    @Provides
    @ViewModelScoped
    override fun provideRoomViewModel(
        postRoom: PostRoom,
        getRoom: GetRoom,
        patchRoom: PatchRoom,
        deleteRoom: DeleteRoom,
        getRooms: GetRooms
    ): IRoomViewModel {
        return RoomViewModel(postRoom, getRoom, patchRoom, deleteRoom, getRooms)
    }

    @Provides
    @ViewModelScoped
    override fun provideQuizViewModel(
        postQuiz: PostQuiz,
        getQuiz: GetQuiz,
        patchQuiz: PatchQuiz,
        deleteQuiz: DeleteQuiz,
        getQuestions: GetQuestions
    ): IQuizViewModel {
        return QuizViewModel(postQuiz, getQuiz, patchQuiz, deleteQuiz, getQuestions)
    }

    @Provides
    @ViewModelScoped
    override fun provideResultViewModel(
        postResult: PostResult,
        getResult: GetResult,
        patchResult: PatchResult,
        deleteResult: DeleteResult,
        getResults: GetResults
    ): IResultViewModel {
        return ResultViewModel(postResult, getResult, patchResult, deleteResult, getResults)
    }

    @Provides
    @ViewModelScoped
    override fun provideParticipantViewModel(
        postParticipant: PostParticipant,
        getParticipant: GetParticipant,
        patchParticipant: PatchParticipant,
        deleteParticipant: DeleteParticipant,
        getParticipants: GetParticipants
    ): IParticipantViewModel {
        return ParticipantViewModel(postParticipant, getParticipant, patchParticipant, deleteParticipant, getParticipants)
    }
}