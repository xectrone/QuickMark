package com.xectrone.quickmark.ui.home_screen
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.xectrone.quickmark.R
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    modifier: Modifier = Modifier, 
    item: NoteSelectionListItem, 
    onClick: () -> Unit, 
    onLongClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val elevation by animateDpAsState(
        targetValue = when {
            item.isSelected -> 8.dp
            isPressed -> 4.dp
            else -> 2.dp
        },
        animationSpec = tween(150),
        label = "elevation"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    Card(
        modifier = modifier
            .combinedClickable(
                onClick = { 
                    onClick()
                    isPressed = false
                },
                onLongClick = { 
                    onLongClick()
                    isPressed = false
                }
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = Dimen.Padding.p1)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Dimen.Padding.p3),
        border = if (item.isSelected) 
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary) 
        else 
            null,
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimen.Padding.p3),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            // Header row with title and pin icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pin icon (only show if pinned)
                AnimatedVisibility(
                    visible = item.isPinned,
                    enter = scaleIn(animationSpec = tween(150)) + fadeIn(animationSpec = tween(150)),
                    exit = scaleOut(animationSpec = tween(150)) + fadeOut(animationSpec = tween(150))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_push_pin_24),
                        contentDescription = "Bookmarked note",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimen.Padding.p2),
                    text = item.fileName,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Content preview
            AnimatedVisibility(
                visible = item.fileContent.isNotBlank(),
                enter = expandVertically(animationSpec = tween(200)) + fadeIn(animationSpec = tween(200)),
                exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = Dimen.Padding.p1,
                        bottom = Dimen.Padding.p1
                    ),
                    text = item.fileContent,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Date
            Text(
                modifier = Modifier.padding(top = Dimen.Padding.p1),
                text = Util.formattedDate(item.lastModified),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                softWrap = true
            )
        }
    }
}


