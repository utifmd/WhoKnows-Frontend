package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Answer

/**
 * Mon, 27 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SingleChoiceQuestion(
    options: List<String>,
    answer: Answer?,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier) {

    val selected = answer?.answer
    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(selected) }

    Column(modifier = modifier) {
        options.forEach { text ->
            val onClickHandle = {
                onOptionSelected(text)
                onAnswerSelected(text)
            }
            val optionSelected = text == selectedOption
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = (0.5).dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
                modifier = Modifier.padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionSelected,
                            onClick = onClickHandle
                        )
                        .padding(
                            vertical = 16.dp,
                            horizontal = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = text)
                    RadioButton(
                        selected = optionSelected,
                        onClick = onClickHandle,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}