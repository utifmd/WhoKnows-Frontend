package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
@Composable
fun WhoKnowsTopBar() {
    TopAppBar(
        title = { Text("title") },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, actions = {
            OutlinedTextField(
                value = "searchText",
                onValueChange = onSearchTextChanged,
                placeholder = { Text("placeholder") })
        }) {

    }
}