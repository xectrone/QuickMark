package com.example.quickmark.ui.home_screen
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Alignment
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.domain.Util
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.LocalCustomColorPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(item: NoteSelectionListItem, context: Context, onClick:()->Unit, onLongClick:()->Unit)
{
    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = {onClick()},
                onLongClick = {onLongClick()}
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = Dimen.Padding.p1),
        backgroundColor = MaterialTheme.colors.onBackground,
        shape = RoundedCornerShape(Dimen.Padding.p3),
        border = if (item.isSelected) BorderStroke(1.dp, LocalCustomColorPalette.current.accent) else null,
        elevation = Dimen.TopBar.elevation
    )
    {
        Column(
            modifier = Modifier
                .padding(Dimen.Padding.p3),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start

        )
        {
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = item.note.nameWithoutExtension,
                color = LocalCustomColorPalette.current.primary,
                maxLines = 1,
                style = CustomTypography.textPrimary,
                softWrap = true

            )
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = item.note.readText(),
                color = LocalCustomColorPalette.current.secondary,
                maxLines = 1,
                style = CustomTypography.textSecondary,
                softWrap = true
            )
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = Util.formattedDate(Util.fromTimestamp(item.note.lastModified())),
                color = LocalCustomColorPalette.current.secondary,
                maxLines = 1,
                style = CustomTypography.textTertiary,
                softWrap = true
            )
        }
    }
}


