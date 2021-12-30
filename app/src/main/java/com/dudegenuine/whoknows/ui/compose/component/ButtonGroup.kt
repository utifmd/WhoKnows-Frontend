package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonGroup(
    buttons: Set<String>,
    value: String,
    onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        buttons.forEach { text ->
            Row {
                Text(
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
                )
            }
        }
    }
}