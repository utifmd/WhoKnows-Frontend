package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil
import com.dudegenuine.whoknows.ui.compose.component.GeneralCardView
import com.dudegenuine.whoknows.ui.compose.component.misc.CardFooter

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomItem(
    modifier: Modifier = Modifier,
    state: Room, onPressed: () -> Unit) {

    val length = 20
    val trimmed: String = state.description.replace("\n", "").trim()
    val list: List<String> = trimmed.split(" ")
    val desc: String = list.subList(0, list.size).joinToString(" ").plus("..")

    GeneralCardView(
        modifier = modifier.clickable(onClick = onPressed)) {

        Column(
            modifier = modifier.padding(12.dp)) {

            Text(
                text = state.title,
                style = MaterialTheme.typography.h6)

            Spacer(
                modifier = modifier.height(8.dp))

            Text(
                text = if (list.size > length) desc else trimmed,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.body2
            )

            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(top = 16.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {

                CardFooter(
                    icon = Icons.Default.AccessTime,
                    text = if (!state.expired) "Ongoing since ${ViewUtil.timeAgo(state.createdAt)}"
                    else "Ended ${
                        state.updatedAt?.let {
                            ViewUtil.timeAgo(it)
                        }
                    }"
                )

                Row(
                    modifier = Modifier) {
                    CardFooter(
                        icon = Icons.Default.QuestionAnswer,
                        text = state.questions.size.toString())

                    Spacer(
                        modifier = modifier.width(16.dp))

                    CardFooter(
                        icon = Icons.Default.People,
                        text = state.participants.size.toString()
                    )
                }
            }
        }
    }
}