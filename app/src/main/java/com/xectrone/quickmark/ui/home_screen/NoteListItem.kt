package com.xectrone.quickmark.ui.home_screen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(modifier: Modifier = Modifier, item: NoteSelectionListItem, onClick:()->Unit, onLongClick:()->Unit)
{
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = Dimen.Padding.p1),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Dimen.Padding.p3),
        border = if (item.isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = androidx.compose.material3.CardDefaults.cardElevation(Dimen.TopBar.elevation)
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
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = item.fileContent,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p1),
                text = Util.formattedDate(item.lastModified),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                softWrap = true
            )
        }
    }
}


