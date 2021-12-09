package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.usecase.room.*
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.ui.view.room.RoomViewModel
import com.dudegenuine.whoknows.ui.view.user.UserViewModel

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IViewModelModule {
    fun provideUserViewModel(
        postUser: PostUser,
        getUser: GetUser,
        patchUser: PatchUser,
        deleteUser: DeleteUser,
        getUsers: GetUsers,
        signInUser: SignInUser
    ): UserViewModel

    fun provideRoomViewModel(
        postRoom: PostRoom,
        getRoom: GetRoom,
        patchRoom: PatchRoom,
        deleteRoom: DeleteRoom,
        getRooms: GetRooms
    ): RoomViewModel

//    fun provideQuizViewModel(
//        postQuiz: PostQuiz,
//        getQuiz: GetQuiz,
//        patchQuiz: PatchQuiz,
//        deleteQuiz: DeleteQuiz,
//        getQuestions: GetQuestions
//    ): QuizViewModel
}