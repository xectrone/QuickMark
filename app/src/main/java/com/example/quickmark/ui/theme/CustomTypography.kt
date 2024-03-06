package com.example.quickmark.ui.theme

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

    val heading = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
    )
}