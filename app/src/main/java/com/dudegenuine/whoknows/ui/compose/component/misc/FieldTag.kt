package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FieldTag(
    modifier: Modifier = Modifier,
    key: String,
    value: String,
    editable: Boolean = true,
    onValuePressed: (() -> Unit)? = null){

    val onEditClick: () -> Unit = {
        onValuePressed?.let { onValuePressed() }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(
            modifier = modifier.padding(
                horizontal = 12.dp),
            text = key)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {

            TextButton(
                enabled = editable,
                onClick = onEditClick) {

                Text(text = value)
                if(editable){
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
