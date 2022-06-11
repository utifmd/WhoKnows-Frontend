package com.dudegenuine.whoknows.ux.compose.screen.seperate.main

import android.content.Context
import android.content.Intent
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ux.vm.main.IMainViewModel

interface IMainProps {
    val context: Context
    val router: NavHostController
    val viewModel: IMainViewModel
    val intent: Intent

    var lazyPagingRoomComplete: LazyPagingItems<Room.Complete>
    val lazyPagingRoomCensored: LazyPagingItems<Room.Censored>
    val lazyPagingParticipants: LazyPagingItems<User.Censored>
    val lazyPagingQuizzes : LazyPagingItems<Quiz.Complete>
    val lazyPagingNotification : LazyPagingItems<Notification>
}
