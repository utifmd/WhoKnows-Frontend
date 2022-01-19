package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomItem(
    modifier: Modifier = Modifier, state: Room) {

    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp)) {

        Text(
            text = state.title,
            style = MaterialTheme.typography.h6
        )
        Text(
            text = state.description,
            style = MaterialTheme.typography.caption
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()) {
            Text(
                text = if (state.expired) "Expired" else "Ongoing",
                color = if (state.expired) MaterialTheme.colors.error else MaterialTheme.colors.secondary,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.People, contentDescription = null)
                Text(
                    text = state.questions.size.toString(),
                    style = MaterialTheme.typography.overline
                )

                Icon(imageVector = Icons.Default.Assignment, contentDescription = null)
                Text(
                    text = state.participants.size.toString(),
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}