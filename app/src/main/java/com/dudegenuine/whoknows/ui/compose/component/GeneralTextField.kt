package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun GeneralTextField(
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    asPassword: Boolean = false,
    readOnly: Boolean = false,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leads: ImageVector? = null,
    tails: Any? = null,
    isExpand: Boolean = false,
    onTailPressed: (() -> Unit)? = null,
    onExpanderSelected: ((Int) -> Unit)? = null,
    onExpanderDismiss: (() -> Unit)? = null){

    val onExpanderClicked: (Int) -> Unit = { i ->
        onExpanderSelected?.let { onExpanderSelected(i) }
    }

    val onExpanderDismissed: () -> Unit = {
        onExpanderDismiss?.let { onExpanderDismiss() }
    }

    val onTailClicked: () -> Unit = {
        onTailPressed?.let { onTailPressed() }
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label)},
        leadingIcon = {
            if (leads != null){
                Icon(
                    imageVector = leads,
                    contentDescription = "leads-$label",
                    modifier = Modifier.clickable(
                        onClick = onTailClicked))
            }
        },
        trailingIcon = {
            when (tails){
                is ImageVector -> Icon(
                    imageVector = tails,
                    contentDescription = "tails-$label",
                    modifier = Modifier.clickable(
                        onClick = onTailClicked))
                is String -> TextButton(
                    onClick = onTailClicked) {
                    Text(
                        text = tails
                    )
                }
            }},
        singleLine = singleLine,
        readOnly = readOnly,
        visualTransformation = if (asPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
        modifier = modifier.fillMaxWidth()
    )

    DropdownMenu(
        expanded = isExpand,
        onDismissRequest = onExpanderDismissed) {
        for (i in 5..125 step 15){
            DropdownMenuItem(
                onClick = { onExpanderClicked(i) }) {
                Text(
                    text = i.toString())
            }
        }
    }
}
