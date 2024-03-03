package com.example.quickmark.ui.home_screen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.combinedClickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(item: NoteSelectionListItem, onClick:()->Unit, onLongClick:()->Unit)
{
    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = {onClick()},
                onLongClick = {onLongClick()}
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .absolutePadding(5.dp,5.dp,5.dp,0.dp),
        backgroundColor = MaterialTheme.colors.onBackground,
        shape = RoundedCornerShape(6.dp),
        border = if (item.isSelected) BorderStroke(1.dp,MaterialTheme.colors.secondary) else null
    )
    {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,

        )
        {
            Text(
                text = item.note.name,
                color = MaterialTheme.colors.primary,
                maxLines = 1,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold

            )
            Text(
                text = item.note.readText(),
                color = MaterialTheme.colors.primaryVariant,
                maxLines = 2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Justify

            )
        }
        
    }


}


