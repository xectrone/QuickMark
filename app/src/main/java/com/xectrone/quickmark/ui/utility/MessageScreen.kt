package com.xectrone.quickmark.ui.utility

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette

@Composable
fun MessageScreen(message:String) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(modifier = Modifier
            .padding(Dimen.Padding.p7),
            text = message,
            style = MaterialTheme.typography.h6,
            color = LocalCustomColorPalette.current.secondary,
            textAlign = TextAlign.Center
        )
    }
}