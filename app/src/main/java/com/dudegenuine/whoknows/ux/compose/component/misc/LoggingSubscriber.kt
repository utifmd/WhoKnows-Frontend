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
    val TAG: String = "LoggingSubscriber"

    LaunchedEffect(Unit){ child.screenState.collectLatest(parent::onScreenStateChange) }
    LaunchedEffect(child.state){ child.state.apply(parent::onStateChange) }
}