package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ux.compose.component.GeneralCardView
import com.dudegenuine.whoknows.ux.compose.component.misc.CardFooter

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomItem(
    modifier: Modifier = Modifier, model: Room,
    onImpressed: () -> Unit, onPressed: (() -> Unit)? = null) {

    val title by remember{
        derivedStateOf{ when(model){
            is Room.Complete -> model.title
            is Room.Censored -> model.title }
        }
    }

    val desc by remember{
        derivedStateOf{ when(model){
            is Room.Complete -> model.description
            is Room.Censored -> model.description }
        }
    }

    val (impress, setImpress) = rememberSaveable {
        when(model) {
            is Room.Complete -> mutableStateOf(model.impressed)
            is Room.Censored -> mutableStateOf(model.impressed)
        }
    }
    GeneralCardView(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = onPressed != null,
                onClick = { onPressed?.invoke() })) {
        Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(desc,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
            }
            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {

                when (model) {
                    is Room.Censored -> Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        CardFooter(
                            icon = Icons.Filled.People,
                            text = model.participantSize.toString())
                        Spacer(Modifier.size(ButtonDefaults.IconSize))
                        CardFooter(
                            text = "${model.impressionSize} ${if (model.impressionSize > 1) "like\'s" else "like"}",
                            icon = if (impress) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            color = if (impress) MaterialTheme.colors.error else null,
                            onIconClick = onImpressed
                        )
                    }
                    is Room.Complete -> Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        Row(Modifier){
                            CardFooter(
                                icon = Icons.Filled.Task,
                                text = model.questions.size.toString())
                            Spacer(Modifier.size(ButtonDefaults.IconSize))

                            CardFooter(
                                icon = Icons.Default.People,
                                text = model.participants.size.toString()
                            )
                        }

                        CardFooter(
                            text = "${model.impressionSize} ${if(model.impressionSize > 1) "like\'s" else "like"}",
                            icon = if(impress) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            color = if(impress) MaterialTheme.colors.error else null){
                            setImpress(!impress)
                            Log.d("RoomItem", "setImpress $impress")
                        }
                    }
                }
            }
        }
    }
}