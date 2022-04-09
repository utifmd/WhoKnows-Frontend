package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
@Composable
fun SearchTopBar(
    title: String,
    modifier: Modifier = Modifier,
    searchValue: String,
    placeholderText: String,
    onSearchTextChanged: (String) -> Unit,
    onClearPressed: () -> Unit,
    onBackPressed: (() -> Unit)? = null,
    light: Boolean = true,
    onTailPressed: (() -> Unit) ? = null,
    tails: ImageVector? = null) {

    //val keyboardController = LocalSoftwareKeyboardController.current
    //val focusRequester = remember { FocusRequester() }
    val showClearButton by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(title) },
        elevation = (0.5).dp,
        backgroundColor = if (light) MaterialTheme.colors.surface
            else MaterialTheme.colors.primary,
        navigationIcon = if (onBackPressed != null) {
            {
                IconButton(onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier,
                        tint = if (light) MaterialTheme.colors.onSurface
                        else MaterialTheme.colors.onPrimary,
                        contentDescription = null
                    )
                }
            }
        } else null,
        actions = {
            OutlinedTextField(
                modifier = modifier.fillMaxWidth().padding(vertical = 2.dp)
                    /*.onFocusChanged { focusState ->
                        showClearButton = (focusState.isFocused)
                    }
                    .focusRequester(focusRequester)*/,
                value = searchValue,
                onValueChange = onSearchTextChanged,
                placeholder = {
                    Text(placeholderText)/*,
                        color = if (light) MaterialTheme.colors.onSurface
                            else MaterialTheme.colors.onPrimary
                    )*/
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                ),
                trailingIcon = {
                    Row {
                        AnimatedVisibility(
                            visible = showClearButton,
                            enter = fadeIn(),
                            exit = fadeOut()) {
                            IconButton(onClearPressed) {
                                Icon(Icons.Filled.Close,
                                    contentDescription = null
                                )
                            }
                        }
                        if (tails != null){
                            IconButton({ onTailPressed?.invoke() }) {
                                Icon(tails,
                                    tint = if (light) MaterialTheme.colors.onSurface
                                        else MaterialTheme.colors.onPrimary,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                },
                maxLines = 1,
                singleLine = true,
                /*keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions
                    { keyboardController?.hide() },*/
            )
        }
    )

    /*LaunchedEffect(Unit)
        { focusRequester.requestFocus() }*/
}