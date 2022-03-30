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
    model: Room, onPressed: (() -> Unit)? = null) {

    val title = if (model is Room.Censored) model.title
        else (model as Room.Complete).title

    val desc: String = if (model is Room.Censored) model.description
        else (model as Room.Complete).description.replace("\n", "").trim()

    GeneralCardView(
        modifier = modifier.clickable(
            enabled = onPressed != null,
            onClick = { onPressed?.invoke() })) {

        Column(modifier.padding(12.dp)) {
            Text(title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6)

            Spacer(modifier.height(8.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(desc,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
            }

            when (model) {
                is Room.Censored -> {
                    Spacer(modifier.size(16.dp))
                    CardFooter(
                        icon = Icons.Default.Timer,
                        text = "${model.minute} minute\'s"
                    )
                }
                is Room.Complete -> {
                    Row(modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {

                        CardFooter(
                            icon = if (!model.expired) Icons.Default.LockOpen else Icons.Default.Lock,
                            text = if (!model.expired) "Opened ${timeAgo(model.createdAt)}"
                                else "Closed ${model.updatedAt?.let { timeAgo(it) }}"
                        )

                        Row(Modifier) {
                            CardFooter(
                                icon = Icons.Default.QuestionAnswer,
                                text = model.questions.size.toString())

                            Spacer(modifier.width(16.dp))

                            CardFooter(
                                icon = Icons.Default.People,
                                text = model.participants.size.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}