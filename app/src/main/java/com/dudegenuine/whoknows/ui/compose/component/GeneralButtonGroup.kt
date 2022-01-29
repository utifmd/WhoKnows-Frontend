package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun GeneralButtonGroup(
    modifier: Modifier = Modifier,
    buttons: Set<String>,
    value: String,
    onValueChange: (String) -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth()) {
        buttons.forEach { text ->
            Button(
                modifier = modifier.weight(1f),
                shape = RectangleShape,
                enabled = text != value,
                onClick = { onValueChange(text) }) {
                Text(text = text)
            }

            /*Row(
                modifier = modifier.fillMaxWidth()) {

                *//*Text(
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
                )*//*
            }*/
        }
    }
}