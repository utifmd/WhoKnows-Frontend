package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {

        CircularProgressIndicator()
    }
}