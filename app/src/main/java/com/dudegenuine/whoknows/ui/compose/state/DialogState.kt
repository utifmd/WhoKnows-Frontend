package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class DialogState(
    val about: String? = null,
    val disclaimer: String? = null,
    val onDismissed: (() -> Unit)? = null,
    val onSubmitted: (() -> Unit)? = null){

    var button by mutableStateOf(about?.split(" ")?.get(0) ?: "submit")
    var title by mutableStateOf("Konfirmasi $about")
    var text by mutableStateOf("Pilih $button untuk melanjutkan")
}