package com.xectrone.quickmark.ui.utility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette

@Composable
fun CustomOutlineButton(
    modifier: Modifier = Modifier,
    text1: String, // label
    text2: String, // emoji or secondary text
    color: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = color),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = color
        ),
        onClick = { onClick() },
        elevation = ButtonDefaults.buttonElevation(0.dp),
        shape = RoundedCornerShape(Dimen.Padding.p3)
    ) {
        Column(
            modifier = Modifier.padding(Dimen.Padding.p1),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.background(Color.Transparent),
                text = text2,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = color,
            )
            Spacer(modifier = Modifier.height(Dimen.Padding.p2))
        Text(
            modifier = Modifier.background(Color.Transparent),
                text = text1,
                style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
        )
            Spacer(modifier = Modifier.height(Dimen.Padding.p2))
        }
    }
}