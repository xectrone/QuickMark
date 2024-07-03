package com.xectrone.quickmark.ui.home_screen
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
import androidx.compose.ui.text.style.TextOverflow
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.ui.theme.CustomTypography
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(item: NoteSelectionListItem, onClick:()->Unit, onLongClick:()->Unit)
{
    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
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
                text = item.fileName,
                color = LocalCustomColorPalette.current.primary,
                maxLines = 1,
                style = CustomTypography.textPrimary,
                overflow = TextOverflow.Ellipsis

            )
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = item.fileContent,
                color = LocalCustomColorPalette.current.secondary,
                maxLines = 1,
                style = CustomTypography.textSecondary,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = Util.formattedDate(item.lastModified),
                color = LocalCustomColorPalette.current.secondary,
                maxLines = 1,
                style = CustomTypography.textTertiary,
                softWrap = true
            )
        }
    }
}


