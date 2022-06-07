package com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ViewUtil

/**
 * Wed, 30 Mar 2022
 * WhoKnows by utifmd
 **/
@Composable
fun QuestItem(
    modifier: Modifier = Modifier, model: Quiz.Complete) {
    Row(modifier.width(246.dp).background(
        color = MaterialTheme.colors.onSurface.copy(
            alpha = if (MaterialTheme.colors.isLight) 0.04f else 0.06f),
        shape = MaterialTheme.shapes.small)
    ) {
        val text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.SemiBold)
            ) {

                append(model.user?.fullName ?: "unknown")
            }

            withStyle(
                SpanStyle(
                    MaterialTheme.colors.onSurface.copy(0.5f),
                    fontStyle = FontStyle.Italic, fontSize = 11.sp)
            ) {

                append(" @${model.user?.username ?: "@unknown"}")
                append(" ${ViewUtil.timeAgo(model.createdAt)}")
            }

            append("\n")
            append(model.question)
        }

        Text(text, //style = MaterialTheme.typography.subtitle1, maxLines = 5, overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = 24.dp,
                    horizontal = 16.dp
                )
        )
    }
}