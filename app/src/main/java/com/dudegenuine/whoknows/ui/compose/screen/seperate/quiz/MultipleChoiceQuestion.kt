package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
fun MultipleChoiceQuestion(
    options: List<String>,
    answer: Answer?,
    onAnswerSelected: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        for (option in options) {
            val checkedState = remember(answer) {
                val selectedOption = answer?.answers?.contains(option)

                mutableStateOf(selectedOption ?: false)
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable(
                        onClick = {
                            checkedState.value = !checkedState.value
                            onAnswerSelected(option, checkedState.value)
                        }
                    )
                    .padding(
                        vertical = 8.dp,
                        horizontal = 12.dp
                    ),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {

                Text(text = option)

                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { selected ->
                        checkedState.value = selected

                        onAnswerSelected(option, selected)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.primary
                    ),
                )
            }
        }
    }
}