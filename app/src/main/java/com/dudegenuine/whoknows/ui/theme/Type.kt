package com.dudegenuine.whoknows.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dudegenuine.whoknows.R

val WhoKnowsFont = FontFamily(
    Font(R.font.roboto_condensed_regular/*, style = FontStyle.Normal*/),
    Font(R.font.roboto_condensed_italic, style = FontStyle.Italic),
    Font(R.font.roboto_condensed_bold, weight = FontWeight.Bold),
    Font(R.font.roboto_condensed_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.roboto_condensed_light, weight = FontWeight.Light),
    Font(R.font.roboto_condensed_light_italic, weight = FontWeight.Light, style = FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = WhoKnowsFont, //FontFamily.Default
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    /* Other default text styles to override */
    button = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    /* Additional */
    h1 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    h6 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    body2 = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    overline = TextStyle(
        fontFamily = WhoKnowsFont,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp
    )
/*

* */
)