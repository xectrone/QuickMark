package com.xectrone.quickmark.ui.home_screen

import android.net.Uri
import java.time.LocalDateTime

data class NoteSelectionListItem(
    val fileName: String,
    val fileContent: String,
    val lastModified: LocalDateTime,
    val fileUri: Uri,
    val isSelected: Boolean = false
)
