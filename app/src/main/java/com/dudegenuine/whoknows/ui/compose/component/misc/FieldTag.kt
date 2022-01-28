package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FieldTag(
    modifier: Modifier = Modifier,
    key: String,
    value: String,
    editable: Boolean = true,
    censored: Boolean = false,
    isDivide: Boolean = true,
    color: Color? = null,
    onValuePressed: (() -> Unit)? = null){

    val onEditClick: () -> Unit = {
        onValuePressed?.let { onValuePressed() }
    }

    Column(
        modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                modifier = modifier.padding(
                    horizontal = 12.dp
                ),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = color ?: MaterialTheme.colors.onSurface,
                text = key
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {

                TextButton(
                    enabled = editable,
                    onClick = onEditClick
                ) {

                    Text( /*value.map { "*" }.fold("") { a, _ -> "$a*" }*/
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = if (censored) "*".repeat(value.length) else value
                    )

                    if (editable) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        if (isDivide) {
            Divider(
                modifier = modifier.fillMaxWidth(), thickness = (0.5).dp
            )
        }
    }
}
