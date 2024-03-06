package com.example.quickmark.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorPalette(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val backgroundSecondary: Color = Color.Unspecified,
    val surface: Color = Color.Unspecified,
    val accent: Color = Color.Unspecified

)

val LocalCustomColorPalette = staticCompositionLocalOf { CustomColorPalette() }

val OnLightCustomColorPalette = CustomColorPalette(
    primary = Color(color = 0xFF000000),
    secondary = Color(color = 0xFF5D5E5E),
    tertiary = Color(color = 0xFF828484),
    background = Color(color = 0xFFF6F8F8),
    backgroundSecondary = Color(color = 0xFFFFFFFF),
    surface = Color(color = 0xFFFFFFFF),
    accent = Color(color = 0xFF03A9F4)
)

val OnDarkCustomColorPalette = CustomColorPalette(
    primary = Color(color = 0xFF000000),
    secondary = Color(color = 0xFF5D5E5E),
    tertiary = Color(color = 0xFF828484),
    background = Color(color = 0xFFF6F8F8),
    backgroundSecondary = Color(color = 0xFFFFFFFF),
    surface = Color(color = 0xFFFFFFFF),
    accent = Color(color = 0xFF03A9F4)
)