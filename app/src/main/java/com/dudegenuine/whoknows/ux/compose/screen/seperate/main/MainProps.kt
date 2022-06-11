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

/**
 * Tue, 07 Jun 2022
 * WhoKnows by utifmd
 **/
class MainProps(
    /*override val state: ResourceState,
    override val store: ResourceState.Store,*/
    override val context: Context,
    override val router: NavHostController,
    override val viewModel: IMainViewModel,
    override val intent: Intent,

    override var lazyPagingRoomComplete: LazyPagingItems<Room.Complete>,
    override val lazyPagingRoomCensored: LazyPagingItems<Room.Censored>,
    override val lazyPagingParticipants: LazyPagingItems<User.Censored>,
    override val lazyPagingQuizzes: LazyPagingItems<Quiz.Complete>,
    override val lazyPagingNotification: LazyPagingItems<Notification>
): IMainProps