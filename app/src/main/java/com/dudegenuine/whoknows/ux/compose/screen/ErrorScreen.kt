package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onPressed: (() -> Unit)? = null) {
    Box(
        modifier = if(isSnack) Modifier.fillMaxWidth().padding(vertical = 12.dp) else modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {

        Text(
            text = message,
            style = if(isSnack) MaterialTheme.typography.caption else MaterialTheme.typography.body1,
            color = if(isDanger) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.Center)
                .clickable(enabled = onPressed != null, onClick =  { onPressed?.invoke() })
        )
    }
}