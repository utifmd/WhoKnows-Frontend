package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Fri, 17 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun ForegroundTopStack(content: @Composable () -> Unit){
    Surface(
        color = Color.White,
        modifier = Modifier
            .requiredHeight(600.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(60.dp).copy(
            topStart = ZeroCornerSize,
            topEnd = ZeroCornerSize)) {
        content()
    }
}

@Composable
fun ForegroundBottomStack(modifier: Modifier = Modifier, contentModifier: Modifier = Modifier, content: @Composable () -> Unit){
    Surface(
        color = Color.White,
        modifier = modifier
            .height(600.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp).copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize)) {
        content()
    }
}

@Composable
fun BackgroundStack(content: @Composable () -> Unit){
    Surface(
        color = Color.DarkGray,
        modifier = Modifier.fillMaxSize()) {
        content()
    }
}