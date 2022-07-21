package com.dudegenuine.whoknows.ux.compose.screen.seperate.notification

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Notification
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralImage

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationItem(
    model: Notification,
    modifier: Modifier = Modifier,
    onItemLongPressed: () -> Unit, onItemPressed: (Boolean) -> Unit) {
    val (seen, setSeen) = rememberSaveable{ mutableStateOf(model.seen) }
    val exclusive by remember{ mutableStateOf(model.recipientIds.isEmpty()) }

    fun onSeen(){
        onItemPressed(!seen)
        setSeen(true)
    }
    Box(modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = onItemLongPressed,
                onClick = ::onSeen,
                enabled = exclusive)) {

        Row(modifier
                .fillMaxSize()
                .padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Surface(modifier.size(36.dp), shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)) {

                GeneralImage(
                    modifier = modifier.fillMaxSize(),
                    data = model.sender?.profileUrl ?: "",
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Icon(Icons.Default.Person,
                            modifier = modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            tint = MaterialTheme.colors.secondaryVariant,
                            contentDescription = null
                        )
                    }
                )
            }

            Column {
                Text((model.sender?.fullName ?: stringResource(R.string.unknown)) +" - "+ model.event,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (seen)
                        MaterialTheme.colors.onSurface.copy(alpha = 0.5f) else if(!exclusive)
                            MaterialTheme.colors.onBackground else
                                MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.caption)

                Text(timeAgo(model.createdAt),
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}