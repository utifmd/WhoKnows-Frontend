package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import com.dudegenuine.usecase.file.UploadFile
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.usecase.result.*
import com.dudegenuine.usecase.room.*
import com.dudegenuine.usecase.user.*
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
        uploadFile: UploadFile,
        postUser: PostUser,
        getUser: GetUser,
        patchUser: PatchUser,
        deleteUser: DeleteUser,
        getUsers: GetUsers,
        signInUser: SignInUser
    ): IUserViewModel

    fun provideRoomViewModel(
        postRoom: PostRoom,
        getRoom: GetRoom,
        patchRoom: PatchRoom,
        deleteRoom: DeleteRoom,
        getRooms: GetRooms
    ): IRoomViewModel

    fun provideQuizViewModel(
        uploadFile: UploadFile,
        postQuiz: PostQuiz,
        getQuiz: GetQuiz,
        patchQuiz: PatchQuiz,
        deleteQuiz: DeleteQuiz,
        getQuestions: GetQuestions
    ): IQuizViewModel

    fun provideResultViewModel(
        postResult: PostResult,
        getResult: GetResult,
        patchResult: PatchResult,
        deleteResult: DeleteResult,
        getResults: GetResults
    ): IResultViewModel

    fun provideParticipantViewModel(
        postParticipant: PostParticipant,
        getParticipant: GetParticipant,
        patchParticipant: PatchParticipant,
        deleteParticipant: DeleteParticipant,
        getParticipants: GetParticipants
    ): IParticipantViewModel
}