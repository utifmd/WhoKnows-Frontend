package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Result
import com.dudegenuine.whoknows.R

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    state: Result,
    onDonePressed: (() -> Unit)? = null) {

    Box(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {

        Column(modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround) {

            Text(state.user?.fullName ?: "Unknown",
                style = MaterialTheme.typography.h3)

            state.let {
                Text(it.score.toString(),
                    style = MaterialTheme.typography.h1)

                Column {
                    if (it.wrongQuiz.isNotEmpty()) Column {
                        Text("Wrong quizzes:",
                            style = MaterialTheme.typography.subtitle1)

                        it.wrongQuiz.mapIndexed { idx, it ->
                            Text("${idx +1}. $it",
                                style = MaterialTheme.typography.caption)
                        }
                    }
                    if (it.correctQuiz.isNotEmpty()) Column {
                        Spacer(modifier.size(24.dp))
                        Text("Correct quizzes:",
                            style = MaterialTheme.typography.subtitle1)

                        it.correctQuiz.mapIndexed { idx, it ->
                            Text("${idx +1}. $it",
                                style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }
            Spacer(modifier.size(24.dp))

            if (onDonePressed != null) Button(onDonePressed) {
                Icon(Icons.Default.DoneAll, contentDescription = null)

                Spacer(modifier.size(ButtonDefaults.IconSize))
                Text(stringResource(R.string.done))
            }
        }
    }
}