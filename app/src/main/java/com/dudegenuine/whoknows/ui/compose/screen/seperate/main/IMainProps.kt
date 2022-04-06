package com.dudegenuine.whoknows.ui.compose.screen.seperate.main

import android.content.Context
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel

interface IMainProps {
    val context: Context
    val router: NavHostController
    val vmMain: IActivityViewModel
    val vmRoom: IRoomViewModel
    val currentUserId: String

    var ownerRoomsPager: LazyPagingItems<Room.Complete>
    val roomsPager: LazyPagingItems<Room.Censored>
    val participantsPager: LazyPagingItems<User.Censored>
    val quizzesPager: LazyPagingItems<Quiz.Complete>
}