package com.dudegenuine.whoknows.ui.compose.state

data class ModalDialog(
    val title: String? = null,
    val text: String? = null,
    val opened: Boolean = false,
    val event: (() -> Unit)? = null,
)