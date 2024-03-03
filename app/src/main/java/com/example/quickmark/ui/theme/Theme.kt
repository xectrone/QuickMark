package com.example.flashup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.encrypsy.ui.theme.Typography
import com.example.remup.ui.theme.Shapes
private val LightColorPalette = lightColors(
    background = Color(0xFFECEFF1),
    onBackground = Color(0xFFFFFFFF),

    primary = Color(0xFF000000),
    primaryVariant = Color(0xFF707070),

    secondary = Color(0xFF03A9F4),
    secondaryVariant = Color(0xFF757575),






    )

private val DarkColorPalette = darkColors(
    background = Color(0xFF000000),
    onBackground = Color(0xFF252525),

    primary = Color(0xFFFFFFFF),
    primaryVariant = Color(0xFFF2F2F2),

    secondary = Color(0xFF03A9F4),
    secondaryVariant = Color(0xFF757575),




    )

@Composable
fun AppNameTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

