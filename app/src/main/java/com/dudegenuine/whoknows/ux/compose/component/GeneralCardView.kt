package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Sat, 29 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralCardView(
    modifier: Modifier = Modifier,
    colorBorder: Color? = null,
    content: @Composable () -> Unit) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = (0.5).dp,
            color = colorBorder ?: MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
        content = content
    )
}