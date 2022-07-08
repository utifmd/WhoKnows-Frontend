package com.dudegenuine.whoknows.ux.compose.component.misc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Wed, 25 May 2022
 * WhoKnows by utifmd
 **/

@Composable
fun LoggingSubscriber(parent: BaseViewModel, child: BaseViewModel) {
    LaunchedEffect(Unit){
        child.screenState.collectLatest(parent::onScreenStateChange)
    }
    LaunchedEffect(child.auth/*.user, child.auth.invalidated*/){
        child.auth.apply(parent::onAuthChange)
    }
    LaunchedEffect(child.participation){
        child.participation.apply(parent::onParticipationChange)
    }
}