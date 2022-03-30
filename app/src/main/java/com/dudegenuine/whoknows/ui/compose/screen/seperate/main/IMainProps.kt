package com.dudegenuine.whoknows.ui.compose.screen.seperate.main

import android.content.Context
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel

interface IMainProps {
    val context: Context
    val vmMain: IActivityViewModel
    val vmUser: IUserViewModel
    val vmRoom: IRoomViewModel
    val router: NavHostController
    
    val roomsPager: LazyPagingItems<Room.Censored>
    val ownerRoomsPager: LazyPagingItems<Room.Complete>
    val participantsPager: LazyPagingItems<User.Censored>
    val quizzesPager: LazyPagingItems<Quiz.Complete>
}