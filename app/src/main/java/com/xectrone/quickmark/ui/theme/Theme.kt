package com.xectrone.quickmark.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette
import com.xectrone.quickmark.ui.theme.OnDarkCustomColorPalette
import com.xectrone.quickmark.ui.theme.OnLightCustomColorPalette

private val LightColorScheme = lightColorScheme(
    primary = OnLightCustomColorPalette.primary,
    secondary = OnLightCustomColorPalette.secondary,
    tertiary = OnLightCustomColorPalette.tertiary,
    background = OnLightCustomColorPalette.background,
    surface = OnLightCustomColorPalette.surface,
    // Add other color mappings as needed
)

private val DarkColorScheme = darkColorScheme(
    primary = OnDarkCustomColorPalette.primary,
    secondary = OnDarkCustomColorPalette.secondary,
    tertiary = OnDarkCustomColorPalette.tertiary,
    background = OnDarkCustomColorPalette.background,
    surface = OnDarkCustomColorPalette.surface,
    // Add other color mappings as needed
)

@Composable
fun QuickMarkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val customColorsPalette = if (darkTheme) {
        OnDarkCustomColorPalette
    } else {
        OnLightCustomColorPalette
    }
    CompositionLocalProvider(LocalCustomColorPalette provides customColorsPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = QuickMarkTypography,
            // TODO: Integrate custom Shapes for Material 3 if needed
            content = content
        )
    }
}

