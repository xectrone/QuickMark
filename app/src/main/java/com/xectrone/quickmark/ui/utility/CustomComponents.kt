package com.xectrone.quickmark.ui.utility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xectrone.quickmark.ui.theme.CustomColorPalette
import com.xectrone.quickmark.ui.theme.CustomTypography
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette


@Composable
fun CustomOutlineButton(modifier: Modifier = Modifier, text:String, onClick: () -> Unit,) {
    Button(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = LocalCustomColorPalette.current.primary),
        colors = ButtonDefaults.buttonColors(backgroundColor = LocalCustomColorPalette.current.background, contentColor = LocalCustomColorPalette.current.primary),
        onClick ={onClick()},
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Text(
            modifier = Modifier.background(Color.Transparent),
            text = text,
            style = CustomTypography.titleSecondary,
            textAlign = TextAlign.Center,
            color = LocalCustomColorPalette.current.primary,

        )
    }
}