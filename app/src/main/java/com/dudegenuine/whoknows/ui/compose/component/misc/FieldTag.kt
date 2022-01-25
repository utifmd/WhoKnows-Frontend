package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
        if(onValuePressed != null) onValuePressed()
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
