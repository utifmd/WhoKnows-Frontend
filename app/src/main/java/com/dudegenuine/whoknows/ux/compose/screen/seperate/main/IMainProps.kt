package com.dudegenuine.whoknows.ux.compose.screen.seperate.main

import android.content.Context
import android.content.Intent
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.vm.main.IActivityViewModel

interface IMainProps {
    val state: ResourceState
    val store: ResourceState.Store
    val context: Context
    val router: NavHostController
    val vmMain: IActivityViewModel
    val intent: Intent

    //val vmRoom: IRoomViewModel
    //val currentUserId: String

    var lazyPagingOwnerRooms: LazyPagingItems<Room.Complete>
    val lazyPagingRooms: LazyPagingItems<Room.Censored>
    val lazyPagingParticipants: LazyPagingItems<User.Censored>
    val lazyPagingQuizzes: LazyPagingItems<Quiz.Complete>
}
