package com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Quiz

/**
 * Mon, 27 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SingleChoiceQuestion(
    modifier: Modifier = Modifier,
    options: List<String>,
    answer: Quiz.Answer.Exact?,
    onAnswerSelected: (String) -> Unit,
) {
    val selected = answer?.answer
    val (selectedOption, onOptionSelected) = remember(answer)
        { mutableStateOf(selected) }

    Column(
        modifier = modifier) {
        options.forEach { text ->
            val onClickHandle = {
                onOptionSelected(text)
                onAnswerSelected(text)
            }

            val optionSelected = text == selectedOption
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = optionSelected,
                        onClick = onClickHandle
                    )
                    .padding(
                        vertical = 8.dp,
                        horizontal = 12.dp
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