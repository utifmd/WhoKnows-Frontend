package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.state.RoomState

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun RoomResultScreen(
    modifier: Modifier = Modifier,
    state: RoomState.BoardingResult,
    // onSharePressed: () -> Unit,
    onDonePressed: () -> Unit) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.h3
        )
        state.data?.let {
            Text(
                text = it.score.toString(),
                style = MaterialTheme.typography.h1
            )
            Column {
                if (it.wrongQuiz.isNotEmpty()) Column {
                    Text(
                        text = "Wrong quizzes:",
                        style = MaterialTheme.typography.subtitle1)
                    it.wrongQuiz.mapIndexed { idx, it ->
                        Text(
                            text = "${idx +1}. $it",
                            style = MaterialTheme.typography.caption)
                    }
                }
                if (it.correctQuiz.isNotEmpty()) Column {
                    Text(
                        text = "Correct quizzes:",
                        style = MaterialTheme.typography.subtitle1)
                    it.correctQuiz.mapIndexed { idx, it ->
                        Text(
                            text = "${idx +1}. $it",
                            style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
        Button(onClick = onDonePressed) {
            Text(text = stringResource(R.string.done))
        }
    }
}