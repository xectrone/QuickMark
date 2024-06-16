package com.example.quickmark.ui.home_screen

import android.net.Uri
import java.io.File
import java.time.LocalDateTime
import java.util.Date

data class NoteSelectionListItem(
    val fileName: String,
    val fileContent: String,
    val lastModified: LocalDateTime,
    val fileUri: Uri,
    val isSelected: Boolean = false
)
