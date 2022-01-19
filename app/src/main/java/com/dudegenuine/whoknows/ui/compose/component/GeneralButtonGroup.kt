package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeneralButtonGroup(
    modifier: Modifier = Modifier,
    buttons: Set<String>,
    value: String,
    onValueChange: (String) -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        buttons.forEach { text ->
            Row(
                modifier = modifier
                    .fillMaxWidth().padding(16.dp)) {
                Button(
                    modifier = modifier.weight(1f),
                    enabled = text != value,
                    onClick = { onValueChange(text) }) {
                    Text(text = text)
                }

                /*Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    color = Color.White,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                size = 5.dp
                            )
                        )
                        .clickable {
                            onValueChange(text)
                        }
                        .background(
                            if (text == value) {
                                MaterialTheme.colors.primary
                            } else {
                                Color.LightGray
                            }
                        )
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp,
                        )
                )*/
            }
        }
    }
}