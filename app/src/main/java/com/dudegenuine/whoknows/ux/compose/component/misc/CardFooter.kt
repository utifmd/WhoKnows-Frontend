package com.dudegenuine.whoknows.ux.compose.component.misc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CardFooter(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    color: Color? = null, onIconClick: (() -> Unit)? = null){

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            modifier = Modifier.clip(CircleShape).clipToBounds()
                .clickable(enabled = onIconClick != null) { onIconClick?.invoke() },
            tint = color ?: MaterialTheme.colors.onBackground,
            imageVector = icon, contentDescription = null)

        Spacer(
            modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = /*color ?: */MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.caption,
            overflow = TextOverflow.Ellipsis
        )
    }
}