package com.dudegenuine.whoknows.ui.compose.screen.seperate.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Notification
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralImage

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    model: Notification, onItemPressed: () -> Unit) {
    var isSeen by remember { mutableStateOf(model.seen) }

    Row(
        modifier = modifier.fillMaxWidth().clickable(
            enabled = !isSeen){
            isSeen = true

            onItemPressed()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        Surface(
            modifier = Modifier.size(36.dp), shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)) {

            GeneralImage(
                modifier = modifier.fillMaxSize(),
                data = model.sender?.profileUrl ?: "",
                placeholder = {
                    Icon(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        imageVector = Icons.Default.Person,
                        tint = MaterialTheme.colors.secondaryVariant,
                        contentDescription = null
                    )
                }
            )
        }

        Column {
            Text((model.sender?.fullName ?: stringResource(R.string.unknown)) +" @"+ model.event,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = if (!isSeen) MaterialTheme.colors.secondaryVariant
                    else MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.caption)

            Text(timeAgo(model.createdAt),
                style = MaterialTheme.typography.overline
            )
        }
    }
}