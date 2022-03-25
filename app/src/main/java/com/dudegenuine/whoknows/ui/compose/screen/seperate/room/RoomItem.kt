package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil.timeAgo
import com.dudegenuine.whoknows.ui.compose.component.GeneralCardView
import com.dudegenuine.whoknows.ui.compose.component.misc.CardFooter

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomItem(
    modifier: Modifier = Modifier,
    censored: Boolean = false,
    state: Room.Complete, onPressed: (() -> Unit)? = null) {

    val desc: String = state.description
        .replace("\n", "").trim()

    GeneralCardView(
        modifier = modifier.clickable(
            enabled = onPressed != null,
            onClick = { onPressed?.invoke() })) {

        Column(
            modifier = modifier.padding(12.dp)) {

            Text(
                text = state.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6)

            Spacer(
                modifier = modifier.height(8.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = desc,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
            }

            if (censored) {
                Spacer(modifier.size(16.dp))
                CardFooter(
                    icon = Icons.Default.Timer,
                    text = "${state.minute} minute\'s"
                )
            } else {
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {

                    CardFooter(
                        icon = if (!state.expired) Icons.Default.LockOpen else Icons.Default.Lock,
                        text = if (!state.expired) "Opened ${timeAgo(state.createdAt)}"
                            else "Closed ${state.updatedAt?.let { timeAgo(it) }}"
                    )

                    Row(Modifier) {
                        CardFooter(
                            icon = Icons.Default.QuestionAnswer,
                            text = state.questions.size.toString())

                        Spacer(modifier.width(16.dp))

                        CardFooter(
                            icon = Icons.Default.People,
                            text = state.participants.size.toString()
                        )
                    }
                }
            }
        }
    }
}