package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar

/**
 * Wed, 29 Dec 2021
 * WhoKnows by utifmd
 **/
@Preview
@Composable
fun RoomHomeScreen() {
    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Recently class\'s",
            )
        },
        content = {
            Header()
            Body()
        }
    )
}

@Composable
private fun Body(){
    // TODO: list of owner rooms
}

@Composable
private fun Header(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = "New class"
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Join with a code")
        }
    }
}

@Composable
private fun ProfileCard(modifier: Modifier = Modifier) {
    Row(
        modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { /* Ignoring onClick */ })
            .padding(16.dp)) {

        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)) {
            // Image goes here
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)) {

            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}