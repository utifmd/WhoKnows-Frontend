package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Wed, 29 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun RoomHomeScreen(
    viewModel: RoomViewModel = hiltViewModel()) {
    val resourceState = viewModel.resourceState.value

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Recently class", //"Recently class\'s",
            )
        },
        content = {
            Column {
                Header()
                Body(resourceState)
            }
        }
    )
}

@Composable
private fun Body(
    resourceState: ResourceState,
    modifier: Modifier = Modifier){

    LazyColumn(
        modifier = modifier.fillMaxWidth()) {
        if (resourceState.loading){
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillParentMaxWidth()
                )
            }
        }
        resourceState.rooms?.forEach {
            item {
                Column(
                    modifier = modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp)) {

                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
        if(resourceState.error.isNotBlank()){
            item {
                Text(
                    text = resourceState.error,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
private fun Header(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }) {
            Text(
                text = "New class"
            )}
        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }) {
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
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)) { /*Image goes here*/
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