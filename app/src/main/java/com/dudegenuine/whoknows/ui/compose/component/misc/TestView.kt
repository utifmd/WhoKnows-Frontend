package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.ui.compose.screen.LoadBoxScreen

/**
 * Thu, 24 Feb 2022
 * WhoKnows by utifmd
 **/
@Preview
@Composable
fun TestView() {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LoadBoxScreen(height = 36.dp)
            LoadBoxScreen(height = 36.dp)
        }
        LoadBoxScreen(height = 130.dp, times = 5)
    }
}