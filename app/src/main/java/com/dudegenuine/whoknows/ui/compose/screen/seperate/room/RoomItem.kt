package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ViewUtil

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomItem(
    modifier: Modifier = Modifier, state: Room) {

    val length = 20
    val trimmed: String = state.description.replace("\n", "").trim()
    val list: List<String> = trimmed.split(" ")
    val desc: String = list.subList(0, list.size).joinToString(" ").plus("..")

    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp)) {

        Text(
            text = state.title,
            style = MaterialTheme.typography.h6)

        Text(
            text = if (list.size > length) desc else trimmed,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            style = MaterialTheme.typography.body2)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Footer(
                icon = Icons.Default.AccessTime,
                text = if (!state.expired) "Ongoing since ${ViewUtil.timeAgo(state.createdAt)}"
                    else "Ended ${
                        state.updatedAt?.let {
                            ViewUtil.timeAgo(it)
                        }
                    }"
            )

            Row {
                Footer(
                    icon = Icons.Default.Assignment, text = state.questions.size.toString())

                Spacer(
                    modifier = Modifier.width(16.dp))

                Footer(
                    icon = Icons.Default.People, text = state.participants.size.toString())
            }
        }
    }
}

@Composable
private fun Footer(icon: ImageVector, text: String, color: Color? = null){
    Row(
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            tint = color ?: MaterialTheme.colors.onSurface,
            imageVector = icon, contentDescription = null)

        Spacer(
            modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = color ?: MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.caption
        )
    }
}