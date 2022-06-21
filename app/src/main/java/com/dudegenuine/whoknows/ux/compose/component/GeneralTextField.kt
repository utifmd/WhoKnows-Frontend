package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    trail: Any? = null,
    isError: Boolean = false,
    isExpand: Boolean = false,
    keyboardActions: KeyboardActions? = null,
    onTailPressed: (() -> Unit)? = null,
    onExpanderSelected: ((Int) -> Unit)? = null,
    onExpanderDismiss: (() -> Unit)? = null){

    val isPassVisible = remember { mutableStateOf(true) }

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
        leadingIcon =
            if (leads != null) {
                {
                    Icon(/*
                        modifier = modifier.clickable(
                            onClick = onTailClicked
                        ),*/
                        imageVector = leads,
                        contentDescription = null
                    )
                }
            }
            else null,
        trailingIcon = {
            AnimatedVisibility(
                visible = trail != null,
                enter = fadeIn(), exit = fadeOut()) {
                Row {
                    if(asPassword){
                        IconButton({ isPassVisible.value = !isPassVisible.value }) {
                            Icon(if(!isPassVisible.value)
                                Icons.Filled.Visibility else
                                    Icons.Filled.VisibilityOff, null)
                        }
                    }
                    when (trail){
                        is ImageVector -> IconButton(onTailClicked){ Icon(trail, contentDescription = null) }
                        is String -> TextButton(onTailClicked) { Text(trail) }
                    }
                }
            }
        },
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        isError = isError,
        singleLine = singleLine,
        readOnly = readOnly,
        visualTransformation = if (asPassword) {
            if(isPassVisible.value) PasswordVisualTransformation()
            else VisualTransformation.None
        } else VisualTransformation.None,
        modifier = modifier.fillMaxWidth()
    )
    DropdownMenu(
        expanded = isExpand,
        onDismissRequest = onExpanderDismissed) {
        for (i in 5..125 step 15){
            DropdownMenuItem(
                onClick = { onExpanderClicked(i) }) {
                Text("$i min")
            }
        }
    }
}
