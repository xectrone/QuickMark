package com.xectrone.quickmark.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object CustomTypography  {

    @Composable

    fun textPrimary(): TextStyle{
        return TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
//        letterSpacing = 1.25.sp,
        color = LocalCustomColorPalette.current.primary
        )

    }


    @Composable
    fun textSecondary(): TextStyle{
        return TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
//        letterSpacing = 0.4.sp,
        color = LocalCustomColorPalette.current.primary
        )

    }

    @Composable
    fun textTertiary(): TextStyle{
        return TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
//        letterSpacing = 1.5.sp,
        color = LocalCustomColorPalette.current.primary
        )

    }

    @Composable
    fun h2(): TextStyle{
        return TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp,
        color = LocalCustomColorPalette.current.primary
        )

    }

    @Composable
    fun title(): TextStyle{
        return TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp,
        color = LocalCustomColorPalette.current.primary
        )
    }

    @Composable
    fun titleSecondary(): TextStyle{
        return TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp,
        color = LocalCustomColorPalette.current.primary)
    }

    @Composable
    fun body(): TextStyle{
        return TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 24.sp,
        color = LocalCustomColorPalette.current.primary
        )
    }


}