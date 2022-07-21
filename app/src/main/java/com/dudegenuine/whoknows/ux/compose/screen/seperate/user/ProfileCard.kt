package com.dudegenuine.whoknows.ux.compose.screen.seperate.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.ux.compose.component.GeneralImage
import com.dudegenuine.whoknows.ux.theme.SmoothBackground

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ProfileCard(
    modifier: Modifier = Modifier,
    colorBorder: Color? = null,
    name: String, desc: String, data: String,
    onPressed: (() -> Unit)? = null,
    onLongPressed: (() -> Unit)? = null) {

    val content: @Composable () -> Unit = {
        Row(modifier.padding(8.dp)) {
            Surface(modifier.size(50.dp),
                shape = CircleShape,
                color = SmoothBackground) {

                GeneralImage(modifier.fillMaxSize(),
                    data = data,
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Icon(
                            Icons.Default.Person,
                            modifier = modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            tint = MaterialTheme.colors.secondaryVariant, contentDescription = null
                        )
                    }
                )
            }
            Column(modifier.fillMaxHeight().padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center) {
                Text(name, style = MaterialTheme.typography.body1)

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(desc, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
    Surface(
        modifier = modifier.fillMaxWidth().combinedClickable(
            enabled = onPressed != null && onLongPressed != null,
            onClick = { onPressed?.invoke() },
            onLongClick = onLongPressed
        ),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = (0.5).dp,
            color = colorBorder ?: MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
        content = content
    )
}