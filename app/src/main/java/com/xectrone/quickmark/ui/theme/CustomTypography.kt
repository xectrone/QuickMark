package com.xectrone.quickmark.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object CustomTypography {

    val textPrimary = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
//        letterSpacing = 1.25.sp
    )

    val textSecondary = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
//        letterSpacing = 0.4.sp
    )

    val textTertiary = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
//        letterSpacing = 1.5.sp
    )

    val h2 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
    )

    val title = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    )

    val titleSecondary = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp
    )


    val body = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 24.sp
    )

}