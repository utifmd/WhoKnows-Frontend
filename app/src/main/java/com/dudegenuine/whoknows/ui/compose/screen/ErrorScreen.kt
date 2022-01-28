package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String,
    isDanger: Boolean = true,
    isSnack: Boolean = false,
) {
    Box(
        modifier = if(isSnack) modifier.fillMaxWidth().padding(vertical = 16.dp) else modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {

        Text(
            text = message,
            style = if(isSnack) MaterialTheme.typography.caption else TextStyle.Default,
            color = if(isDanger) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.Center)
        )
    }
}