package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.ui.theme.DarkShimmerColorShades
import com.dudegenuine.whoknows.ui.theme.LightShimmerColorShades

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier, isSnack: Boolean = false) {

    Box(if(isSnack)
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    else modifier.fillMaxSize(),

        contentAlignment = Alignment.Center) {

        CircularProgressIndicator()
    }
}

@Composable
private fun Shimmer(content: @Composable (Brush) -> Unit){
    val transition = rememberInfiniteTransition()
    val transitionAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing),

            RepeatMode.Reverse
        )
    )
    /*MaterialTheme.colors.onSurface.copy(
        alpha = if (MaterialTheme.colors.isLight) 0.04f else 0.06f)*/
    val brush = Brush.linearGradient(
        colors = if (MaterialTheme.colors.isLight) LightShimmerColorShades
            else DarkShimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(transitionAnim, transitionAnim)
    )

    /*ShimmerItem(brush = brush)*/
    content(brush)
}

@Composable
fun LoadBoxScreen(
    modifier: Modifier = Modifier,
    height: Dp, width: Dp? = null, times: Int? = null){

    Shimmer { brush ->
        Column(
            if (width != null) modifier
                .height(height)
                .width(width)
            else modifier
                .height(height)
                .fillMaxWidth()
        ) {

            if (times != null) repeat(times) { idx ->
                Spacer(
                    modifier
                        .fillMaxWidth()
                        .size(height)
                        .background(
                            brush,
                            shape = MaterialTheme.shapes.small
                        ))

                if (idx <= times) Spacer(modifier.size(4.dp))

            } else Spacer(
                modifier
                    .fillMaxWidth()
                    .size(height)
                    .background(
                        brush,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}