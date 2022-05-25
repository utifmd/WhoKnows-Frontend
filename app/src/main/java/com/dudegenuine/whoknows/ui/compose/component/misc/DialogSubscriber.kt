package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Wed, 25 May 2022
 * WhoKnows by utifmd
 **/

@Composable
fun DialogSubscriber(vmMain: BaseViewModel, vmSubMain: BaseViewModel) {
    LaunchedEffect(Unit){
        vmSubMain.snackMessage.collectLatest(vmMain::onShowSnackBar)
        vmSubMain.dialogContent.collectLatest(vmMain::onShowDialog)
    }
}