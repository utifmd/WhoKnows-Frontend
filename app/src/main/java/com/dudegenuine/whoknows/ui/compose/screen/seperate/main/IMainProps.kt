package com.dudegenuine.whoknows.ui.compose.screen.seperate.main

import android.content.Context
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel

interface IMainProps {
    val context: Context
    val router: NavHostController
    val vmMain: IActivityViewModel
    //val vmRoom: IRoomViewModel
    //val currentUserId: String

    var lazyPagingOwnerRooms: LazyPagingItems<Room.Complete>
    val lazyPagingRooms: LazyPagingItems<Room.Censored>
    val lazyPagingParticipants: LazyPagingItems<User.Censored>
    val lazyPagingQuizzes: LazyPagingItems<Quiz.Complete>
}
